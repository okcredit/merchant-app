package tech.okcredit.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import tech.okcredit.auth.local.AuthLocalSource
import tech.okcredit.auth.remote.AuthApiClient
import tech.okcredit.auth.remote.AuthRemoteSource
import tech.okcredit.auth.remote.model.request.WhatsAppCodeRequest

@Singleton
@Inject
class AuthServiceImpl(
    private val authLocalSourceLazy: Lazy<AuthLocalSource>,
    private val authRemoteSourceLazy: Lazy<AuthRemoteSource>,
) : AuthService {

    private val authLocalSource by lazy { authLocalSourceLazy.value }
    private val authRemoteSource by lazy { authRemoteSourceLazy.value }

    override suspend fun getCurrentMobileOtpToken(): String? {
        return authLocalSource.getCurrentMobileOtpToken()
    }

    override suspend fun getNewMobileOtpToken(): String? {
        return authLocalSource.getNewMobileOtpToken()
    }

    override suspend fun getPassword(): String? {
        return authLocalSource.getPasswordHash()
    }

    override suspend fun getMobile(): String? = authLocalSource.getMobile()

    override fun authState(): Flow<Boolean> =
        authLocalSource.observeAuthStateChanges()

    override suspend fun isAuthenticated(): Boolean = authLocalSource.isAuthenticated()

    override suspend fun requestOtp(
        mobile: String?,
        flowId: String,
        flowType: String,
    ): OtpToken {
        var finalMobile = mobile
        if (mobile != null) {
            finalMobile = mustParseMobile(mobile)
        }
        return authRemoteSource.requestOtp(finalMobile, flowId, flowType)
    }

    override suspend fun verifyOtp(
        token: OtpToken,
        code: String?,
        flowId: String,
        flowType: String,
    ): Boolean {
        return getTokenAfterVerifyingOtp(token, code, flowId, flowType).isEmpty().not()
    }

    override suspend fun resendOtp(
        mobile: String,
        requestMedium: AuthApiClient.RequestOtpMedium,
        otpId: String,
        flowId: String,
        flowType: String,
        destination: AuthApiClient.RetryDestination,
    ) = authRemoteSource.resendOtp(
        mobileNumber = mustParseMobile(mobile = mobile),
        requestMedium = requestMedium,
        otpId = otpId,
        flowId = flowId,
        flowType = flowType,
        destination = destination,
        language = "",
    )

    override suspend fun requestFallbackOptions(mobileNumber: String) =
        authRemoteSource.requestFallbackOptions(
            mobileNumber = mobileNumber,
            language = "",
            distinctId = "",
        )

    override suspend fun whatsappRequestOtp(whatsAppCodeRequest: WhatsAppCodeRequest): OtpToken {
        return authRemoteSource.whatsappRequestOtp(whatsAppCodeRequest)
    }

    override suspend fun authenticate(
        credential: Credential,
        flowId: String,
        flowType: String,
    ): Pair<Boolean, Boolean> { // first=newUser, second=appLock
        var localCredential = credential

        // verify otp for otp credential
        if (credential is Credential.Otp && credential.token.token == null) {
            val token = getTokenAfterVerifyingOtp(
                token = credential.token,
                code = credential.code,
                flowId = flowId,
                flowType = flowType,
            )
            localCredential = credential.copy(token = credential.token.copy(token = token))
        }

        // authenticate with server
        val grant = try {
            authRemoteSource.authenticate(localCredential, flowId)
        } catch (e: Unauthorized) {
            throw Unauthorized()
        }
        // save auth grant
        authLocalSource.setGrant(grant)

        // save mobile of active user
        if (grant.mobile.isNullOrBlank()) {
            throw IllegalStateException("mobile cannot be null for credential grant")
        }
        authLocalSource.setMobile(grant.mobile)

        return Pair(grant.newUser, grant.appLock)
    }

    override suspend fun authenticateRecoveryNumber(
        credential: Credential.Otp,
        flowId: String,
        flowType: String,
    ): String {
        return getTokenAfterVerifyingOtp(
            token = credential.token,
            code = credential.code,
            flowId = flowId,
            flowType = flowType,
        )
    }

    override suspend fun authenticatePhoneChangeCredential(
        credential: Credential,
        flowId: String,
        flowType: String,
    ): Boolean {
        // verify otp for otp credential
        if (credential is Credential.Otp && credential.token.token == null) {
            val token = getTokenAfterVerifyingOtp(
                token = credential.token,
                code = credential.code,
                flowId,
                flowType,
            )
            authLocalSource.setCurrentMobileOTPToken(token)
            return true
        }

        return false
    }

    override suspend fun authenticateNewPhoneNumberCredential(
        req: Credential,
        flowId: String,
        flowType: String,
    ): Boolean {
        // verify otp for otp credential
        if (req is Credential.Otp && req.token.token == null) {
            val token =
                getTokenAfterVerifyingOtp(token = req.token, code = req.code, flowId, flowType)
            authLocalSource.setNewMobileOtpToken(token)
            return true
        }

        return false
    }

    override suspend fun isPasswordSet(): Boolean {
        // check if hash is available locally
        var passwordHash = authLocalSource.getPasswordHash()
        if (passwordHash != null) return true

        // get hash from server
        passwordHash = authRemoteSource.getPasswordHash()
        if (passwordHash.isNullOrBlank()) return false

        // save hash
        authLocalSource.setPasswordHash(passwordHash)
        return true
    }

    override suspend fun syncPassword() {
        val passwordHash = authRemoteSource.getPasswordHash()
        if (passwordHash.isNullOrBlank()) return

        // save hash
        authLocalSource.setPasswordHash(passwordHash)
    }

    override suspend fun setPassword(password: String, businessId: String) {
        // validate password
        if (!isPasswordValid(password)) throw InvalidPassword()

        authRemoteSource.setPassword(password, businessId)
        authLocalSource.setPasswordHash(hashPassword(password))
    }

    override suspend fun verifyPassword(password: String) {
        if (!isPasswordSet()) throw IncorrectPassword()
        if (authLocalSource.getPasswordHash() != hashPassword(password)) throw IncorrectPassword()
    }

    override suspend fun logout(deviceId: String, signOutType: SignOutType) {
        authRemoteSource.logout(deviceId, signOutType)
        authLocalSource.deleteAllExceptMobile()
    }

    override fun getCookieWithExpiryTime(): Flow<Pair<String, Long>> {
        return authLocalSource.getCookieWithExpiryTime()
    }

    private suspend fun getTokenAfterVerifyingOtp(
        token: OtpToken,
        code: String?,
        flowId: String,
        flowType: String,
    ): String {
        return if (code != null) {
            // push token received via sms
            authRemoteSource.verifyOtp(token.id, code, flowId, flowType)
        } else {
            // pull token sent via whatsapp, check status
            var mutableToken = token
            while (mutableToken.token.isNullOrEmpty()) {
                mutableToken = authRemoteSource.checkOtpStatus(mutableToken)
                if (mutableToken.token.isNullOrEmpty()) {
                    withContext(Dispatchers.Default) {
                        delay(200)
                    }
                }
            }
            mutableToken.token!!
        }
    }
}

data class Grant(
    val accessToken: String,
    val refreshToken: String,
    val expireTime: Long,
    val accessTokenCookie: String,
    val newUser: Boolean = false,
    val mobile: String? = null,
    val appLock: Boolean = false,
    val version: Int = AuthRemoteSource.LATEST_SERVER_AUTH_VERSION,
)

// utils
private fun isPasswordValid(password: String): Boolean = password.isNotBlank()

internal fun Grant?.isValid(): Boolean {
    val system = Clock.System.now()
    return this != null && expireTime > system.epochSeconds
}

private fun hashPassword(password: String): String {
    val payload = password + "jhgjmbsuiyoilanjjviakv"
    return password
    // TODO: Add hashing here
    // return BaseEncoding.base64().encode(Hashing.sha256().hashString(payload, Charsets.UTF_8).asBytes())
}

fun mustParseMobile(mobile: String?): String = parseMobile(mobile) ?: throw InvalidMobile()

fun parseMobile(mobile: String?): String? {
    if (mobile == null) return null
    var mutableMobile = mobile.filter { it.isDigit() }
    mutableMobile = mutableMobile.trimStart('0') // remove trailing 0s
    if (mutableMobile.length == 12) {
        mutableMobile =
            mutableMobile.substring(2) // remove "91" if the mobile is 12 digit
    }
    if (mutableMobile.length != 10) return null
    return when (mutableMobile[0]) {
        '9', '8', '7', '6', '5' -> mutableMobile
        else -> null
    }
}

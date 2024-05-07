package tech.okcredit.auth

import kotlinx.coroutines.flow.Flow
import tech.okcredit.auth.remote.AuthApiClient
import tech.okcredit.auth.remote.AuthApiClient.RequestOtpMedium
import tech.okcredit.auth.remote.model.request.WhatsAppCodeRequest
import tech.okcredit.auth.remote.model.response.FallbackOptionResponse
import tech.okcredit.auth.remote.model.response.ResendOtpResponse
import kotlin.coroutines.cancellation.CancellationException

// AuthService handles all authentication related stuff
interface AuthService {

    // get mobile of authenticated merchant, return null if no authenticated merchant
    suspend fun getMobile(): String?

    suspend fun isAuthenticated(): Boolean

    fun authState(): Flow<Boolean>

    suspend fun getCurrentMobileOtpToken(): String?

    suspend fun getNewMobileOtpToken(): String?

    suspend fun getPassword(): String?

    @Throws(InvalidMobile::class, ExcessiveRequestException::class, CancellationException::class)
    suspend fun requestOtp(mobile: String? = null, flowId: String, flowType: String): OtpToken

    @Throws(Unauthorized::class, InvalidOtp::class, ExpiredOtp::class, CancellationException::class)
    suspend fun verifyOtp(token: OtpToken, code: String?, flowId: String, flowType: String): Boolean

    suspend fun resendOtp(
        mobile: String,
        requestMedium: RequestOtpMedium,
        otpId: String,
        flowId: String,
        flowType: String,
        destination: AuthApiClient.RetryDestination,
    ): ResendOtpResponse

    suspend fun requestFallbackOptions(mobileNumber: String): FallbackOptionResponse

    @Throws(InvalidMobile::class, CancellationException::class)
    suspend fun whatsappRequestOtp(whatsAppCodeRequest: WhatsAppCodeRequest): OtpToken

    // returns true on new registration
    @Throws(Unauthorized::class, InvalidOtp::class, ExpiredOtp::class, CancellationException::class)
    suspend fun authenticate(credential: Credential, flowId: String, flowType: String): Pair<Boolean, Boolean>

    @Throws(Unauthorized::class, InvalidOtp::class, ExpiredOtp::class, CancellationException::class)
    suspend fun authenticateRecoveryNumber(credential: Credential.Otp, flowId: String, flowType: String): String

    @Throws(Unauthorized::class, InvalidOtp::class, ExpiredOtp::class, CancellationException::class)
    suspend fun authenticatePhoneChangeCredential(credential: Credential, flowId: String, flowType: String): Boolean

    @Throws(Unauthorized::class, InvalidOtp::class, ExpiredOtp::class, CancellationException::class)
    suspend fun authenticateNewPhoneNumberCredential(req: Credential, flowId: String, flowType: String): Boolean

    @Throws(Unauthorized::class, CancellationException::class)
    suspend fun isPasswordSet(): Boolean

    @Throws(Unauthorized::class, CancellationException::class)
    suspend fun syncPassword()

    @Throws(InvalidPassword::class, Unauthorized::class, CancellationException::class)
    suspend fun setPassword(password: String, businessId: String)

    @Throws(Unauthorized::class, IncorrectPassword::class, CancellationException::class)
    suspend fun verifyPassword(password: String)

    @Throws(Unauthorized::class, CancellationException::class)
    suspend fun logout(deviceId: String, signOutType: SignOutType)

    fun getCookieWithExpiryTime(): Flow<Pair<String, Long>>
}

sealed class Credential {
    data class Otp(val token: OtpToken, val code: String? = null) : Credential()
    data class Truecaller(val payload: String, val signature: String) : Credential()
    data class TruecallerMissedCall(val mobile: String, val accessToken: String) : Credential()
    data class RefreshToken(val refreshToken: String) : Credential()

    companion object {
        fun Credential.getCredentialTypeInString(): String {
            return when (this) {
                is RefreshToken -> "Password"
                is Otp -> "Otp"
                is Truecaller -> "TrueCaller"
                is TruecallerMissedCall -> "truecaller_call"
            }
        }
    }
}

enum class SignOutType(val value: Int) {
    ALL_DEVICES(0),
    SINGLE_DEVICE(1),
}

data class OtpToken(
    val id: String,
    internal val key: String? = null,
    internal val mobile: String? = null,
    internal val otp: String? = null,
    internal val token: String? = null,
    val overallExpiryTime: Int? = null,
    val fallbackOptionsShowTime: Int? = null,
) {
    fun encode(): String {
        return "code: $otp"
    }
}

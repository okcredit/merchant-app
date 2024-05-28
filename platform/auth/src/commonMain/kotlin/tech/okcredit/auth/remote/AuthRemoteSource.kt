package tech.okcredit.auth.remote

import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.network.asError
import okcredit.base.network.getOrThrow
import tech.okcredit.auth.*
import tech.okcredit.auth.remote.AuthApiClient.Companion.OTP_MODE_PUSH
import tech.okcredit.auth.remote.model.request.*
import tech.okcredit.auth.remote.model.response.*
import tech.okcredit.auth.usecases.CookieHelper

@Inject
class AuthRemoteSource(
    private val authApiClientLazy: Lazy<AuthApiClient>,
    private val protectedAuthApiClientLazy: Lazy<Protected>,
    private val cookieHelperLazy: Lazy<CookieHelper>,
) {

    private val authApiClient by lazy { authApiClientLazy.value }
    private val protectedAuthApiClient by lazy { protectedAuthApiClientLazy.value }
    private val cookieHelper by lazy { cookieHelperLazy.value }

    companion object {
        internal const val LATEST_SERVER_AUTH_VERSION = 3
        const val TOO_MANY_REQUESTS_ERROR_CODE = 429
        const val INVALID_MODE_ERROR = "okcredit.auth.invalid_mobile"
        const val INVALID_PASSWORD_ERROR = "okcredit.auth.invalid_password"
        const val INVALID_VERIFICATION_TOKEN_ERROR = "okcredit.auth.invalid_verification_token"
        const val INVALID_OTP_ERROR = "invalid_otp"
        const val OTP_EXPIRED_ERROR = "otp_expired"
    }

    suspend fun authenticate(credential: Credential, flowId: String): Grant = authenticate(
        when (credential) {
            is Credential.Otp -> AuthenticateRequest(
                grant_type = AuthApiClient.GRANT_TYPE_OTP,
                assertion = credential.token.token,
                origin = 0,
                version = LATEST_SERVER_AUTH_VERSION,
            )

            is Credential.Truecaller -> AuthenticateRequest(
                grant_type = AuthApiClient.GRANT_TYPE_TRUECALLER,
                assertion = "${credential.payload}|${credential.signature}",
                origin = 0,
                version = LATEST_SERVER_AUTH_VERSION,
            )

            is Credential.RefreshToken -> AuthenticateRequest(
                grant_type = AuthApiClient.GRANT_TYPE_REFRESH_TOKEN,
                assertion = credential.refreshToken,
                origin = 0,
                version = LATEST_SERVER_AUTH_VERSION,
            )
            is Credential.TruecallerMissedCall -> AuthenticateRequest(
                grant_type = AuthApiClient.GRANT_TYPE_TRUECALLER_CALL,
                assertion = "${credential.accessToken}|${credential.mobile}",
                origin = 0,
                version = LATEST_SERVER_AUTH_VERSION,
            )
        },
        flowId,
    )

    suspend fun logout(deviceId: String, signOutType: SignOutType) {
        protectedAuthApiClient.logout(
            SignOutRequest(
                type = signOutType.value,
                device_id = deviceId,
            ),
        )
    }

    suspend fun requestOtp(mobile: String?, flowId: String, flowType: String): OtpToken {
        var mode = OTP_MODE_PUSH
        if (mobile == null) {
            mode = AuthApiClient.OTP_MODE_PULL
        }

        val request = RequestOtpRequest(
            mode = mode,
            mobile = mobile,
            otpFlowType = flowType,
        )

        val res = authApiClient.requestOtp(request, flowId).getOrThrow()

        return res.toOtp(mobile)
    }

    suspend fun resendOtp(
        mobileNumber: String,
        requestMedium: AuthApiClient.RequestOtpMedium,
        otpId: String,
        flowId: String,
        flowType: String,
        destination: AuthApiClient.RetryDestination,
        language: String,
    ): ResendOtpResponse {
        return authApiClient.resendOtp(
            ResendOtpRequest(
                mobile = mobileNumber,
                intent = requestMedium.key,
                otp_id = otpId,
                language = language,
                mixpanel_distinct_id = "",
                otp_flow_type = flowType,
                destination = destination.key,
            ),
            flowId,
        ).getOrThrow()
    }

    suspend fun requestFallbackOptions(mobileNumber: String, language: String, distinctId: String): FallbackOptionResponse {
        return authApiClient.requestFallbackOptions(
            FallbackOptionRequest(
                mobile = mobileNumber,
                language = language,
                mixpanel_distinct_id = distinctId,
            ),
        ).getOrThrow()
    }

    suspend fun whatsappRequestOtp(whatsAppCodeRequest: WhatsAppCodeRequest): OtpToken {
        return authApiClient.requestWhatsappCode(whatsAppCodeRequest).getOrThrow()
            .let {
                OtpToken(
                    id = it.otp_id,
                    key = it.otp_key,
                    otp = it.otp_code,
                )
            }
    }

    suspend fun verifyOtp(
        otpId: String,
        code: String,
        flowId: String,
        flowType: String,
    ): String {
        val res = authApiClient.verifyOtp(
            VerifyOtpRequest(
                otp_id = otpId,
                otp = code,
                otp_flow_type = flowType,
                mode = OTP_MODE_PUSH,
            ),
            flowId,
        )

        if (res.isSuccessful) {
            return res.body()!!.token
        } else {
            when (res.code) {
                TOO_MANY_REQUESTS_ERROR_CODE -> {
                    throw res.asError().mapCode(
                        TOO_MANY_REQUESTS_ERROR_CODE to TooManyRequests(),
                    )
                }
                else -> {
                    throw res.asError().mapError(
                        INVALID_OTP_ERROR to InvalidOtp(),
                        OTP_EXPIRED_ERROR to ExpiredOtp(),
                    )
                }
            }
        }
    }

    suspend fun checkOtpStatus(token: OtpToken): OtpToken {
        val res = authApiClient.checkOtpStatus(token.id, token.key!!).getOrThrow()
        return res.insertInto(token)
    }

    suspend fun getPasswordHash(): String? {
        val res = protectedAuthApiClient.checkPasswordSet().getOrThrow()
        return if (res.password_set) {
            res.checksum
        } else {
            null
        }
    }

    suspend fun setPassword(password: String, businessId: String) {
        protectedAuthApiClient.setPassword(
            SetPasswordRequest(password),
            businessId,
        )
    }

    private suspend fun authenticate(
        req: AuthenticateRequest,
        flowId: String,
    ): Grant {
        val res = authApiClient.authenticate(req, flowId)
        if (res.isSuccessful) {
            return res.body()!!.toGrant(cookieHelper.getCookie(res.headers))
        } else {
            throw res.asAuthenticationErrors()
        }
    }
}

// api response mappers
internal fun AuthenticateResponse.toGrant(cookie: String): Grant = Grant(
    accessToken = cookie.split(";")[0],
    refreshToken = refresh_token,
    expireTime = Clock.System.now().epochSeconds.plus(expires_in),
    newUser = new_user,
    mobile = mobile,
    appLock = app_lock,
    accessTokenCookie = cookie,
    version = version,
)

internal fun RequestOtpResponse.toOtp(mobile: String?): OtpToken = OtpToken(
    id = otp_id,
    otp = otp,
    key = otp_key,
    mobile = mobile,
    overallExpiryTime = otp_flow_timeout,
    fallbackOptionsShowTime = fallback_options_show_time,
)

internal fun CheckOtpStatusResponse.insertInto(token: OtpToken): OtpToken =
    token.copy(
        token = this.token,
        mobile = mobile,
    )

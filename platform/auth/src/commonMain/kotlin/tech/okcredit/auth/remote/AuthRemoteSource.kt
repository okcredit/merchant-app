package tech.okcredit.auth.remote

import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.DefaultHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.asError
import okcredit.base.network.get
import okcredit.base.network.getOrThrow
import okcredit.base.network.post
import tech.okcredit.auth.Credential
import tech.okcredit.auth.ExpiredOtp
import tech.okcredit.auth.Grant
import tech.okcredit.auth.InvalidOtp
import tech.okcredit.auth.OtpToken
import tech.okcredit.auth.SignOutType
import tech.okcredit.auth.TooManyRequests
import tech.okcredit.auth.asAuthenticationErrors
import tech.okcredit.auth.remote.AuthApiClient.Companion.OTP_MODE_PUSH
import tech.okcredit.auth.remote.model.request.AuthenticateRequest
import tech.okcredit.auth.remote.model.request.FallbackOptionRequest
import tech.okcredit.auth.remote.model.request.RequestOtpRequest
import tech.okcredit.auth.remote.model.request.ResendOtpRequest
import tech.okcredit.auth.remote.model.request.SetPasswordRequest
import tech.okcredit.auth.remote.model.request.SignOutRequest
import tech.okcredit.auth.remote.model.request.VerifyOtpRequest
import tech.okcredit.auth.remote.model.request.WhatsAppCodeRequest
import tech.okcredit.auth.remote.model.request.WhatsAppCodeResponse
import tech.okcredit.auth.remote.model.response.AuthenticateResponse
import tech.okcredit.auth.remote.model.response.CheckOtpStatusResponse
import tech.okcredit.auth.remote.model.response.CheckPasswordSetResponse
import tech.okcredit.auth.remote.model.response.FallbackOptionResponse
import tech.okcredit.auth.remote.model.response.RequestOtpResponse
import tech.okcredit.auth.remote.model.response.ResendOtpResponse
import tech.okcredit.auth.remote.model.response.VerifyOtpResponse
import tech.okcredit.auth.usecases.CookieHelper

@Inject
class AuthRemoteSource(
    private val baseUrl: BaseUrl,
    private val defaultHttpClient: DefaultHttpClient,
    private val authorizedHttpClient: AuthorizedHttpClient,
    private val cookieHelperLazy: Lazy<CookieHelper>,
) {

    private val cookieHelper by lazy { cookieHelperLazy.value }

    companion object {
        internal const val LATEST_SERVER_AUTH_VERSION = 3
        const val TOO_MANY_REQUESTS_ERROR_CODE = 429
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
        authorizedHttpClient.post<SignOutRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "v3/logout",
            requestBody = SignOutRequest(
                type = signOutType.value,
                device_id = deviceId,
            ),
        ).getOrThrow()
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

        return defaultHttpClient.post<RequestOtpRequest, RequestOtpResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp:request",
            requestBody = request,
            headers = mapOf(AuthApiClient.OKC_LOGIN_FLOW_ID to flowId),
        ).getOrThrow().toOtp(mobile)
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
        return defaultHttpClient.post<ResendOtpRequest, ResendOtpResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp/retry",
            requestBody = ResendOtpRequest(
                mobile = mobileNumber,
                intent = requestMedium.key,
                otp_id = otpId,
                language = language,
                mixpanel_distinct_id = "",
                otp_flow_type = flowType,
                destination = destination.key,
            ),
            headers = mapOf(AuthApiClient.OKC_LOGIN_FLOW_ID to flowId),
        ).getOrThrow()
    }

    suspend fun requestFallbackOptions(
        mobileNumber: String,
        language: String,
        distinctId: String,
    ): FallbackOptionResponse {
        return defaultHttpClient.post<FallbackOptionRequest, FallbackOptionResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp/retry/options",
            requestBody = FallbackOptionRequest(
                mobile = mobileNumber,
                language = language,
                mixpanel_distinct_id = distinctId,
            ),
        ).getOrThrow()
    }

    suspend fun whatsappRequestOtp(whatsAppCodeRequest: WhatsAppCodeRequest): OtpToken {
        return defaultHttpClient.post<WhatsAppCodeRequest, WhatsAppCodeResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp/whatsapp/code",
            requestBody = whatsAppCodeRequest,
        ).getOrThrow().let {
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
        val res = defaultHttpClient.post<VerifyOtpRequest, VerifyOtpResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp:verify",
            requestBody = VerifyOtpRequest(
                otp_id = otpId,
                otp = code,
                otp_flow_type = flowType,
                mode = OTP_MODE_PUSH,
            ),
            headers = mapOf(AuthApiClient.OKC_LOGIN_FLOW_ID to flowId),
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
        val response = defaultHttpClient.get<CheckOtpStatusResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v1.0/otp/${token.id}/status",
            queryParams = mapOf("otp_key" to token.key!!),
        ).getOrThrow()
        return response.insertInto(token)
    }

    suspend fun getPasswordHash(): String? {
        val res = authorizedHttpClient.get<CheckPasswordSetResponse>(
            baseUrl = baseUrl,
            endPoint = "v3/password",
        ).getOrThrow()
        return if (res.password_set) {
            res.checksum
        } else {
            null
        }
    }

    suspend fun setPassword(password: String, businessId: String) {
        authorizedHttpClient.post<SetPasswordRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "v3/password",
            requestBody = SetPasswordRequest(password),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }

    private suspend fun authenticate(
        req: AuthenticateRequest,
        flowId: String,
    ): Grant {
        val res = defaultHttpClient.post<AuthenticateRequest, AuthenticateResponse>(
            baseUrl = baseUrl,
            endPoint = "auth/v3/auth",
            requestBody = req,
            headers = mapOf(AuthApiClient.OKC_LOGIN_FLOW_ID to flowId),
        )
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

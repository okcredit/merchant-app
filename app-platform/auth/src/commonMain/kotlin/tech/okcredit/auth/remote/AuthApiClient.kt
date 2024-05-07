package tech.okcredit.auth.remote

import de.jensklingenberg.ktorfit.Response
import de.jensklingenberg.ktorfit.http.*
import tech.okcredit.auth.remote.model.request.*
import tech.okcredit.auth.remote.model.response.*

// api spec
interface AuthApiClient {

    @POST("auth/v3/auth")
    suspend fun authenticate(
        @Body req: AuthenticateRequest,
        @Header(OKC_LOGIN_FLOW_ID) flowId: String,
    ): Response<AuthenticateResponse>

    @POST("auth/v1.0/otp:request")
    suspend fun requestOtp(
        @Body req: RequestOtpRequest,
        @Header(OKC_LOGIN_FLOW_ID) flowId: String,
    ): Response<RequestOtpResponse>

    @POST("auth/v1.0/otp/retry")
    suspend fun resendOtp(
        @Body req: ResendOtpRequest,
        @Header("okc-login-flow-id") flowId: String,
    ): Response<ResendOtpResponse>

    @POST("auth/v1.0/otp/retry/options")
    suspend fun requestFallbackOptions(@Body req: FallbackOptionRequest): Response<FallbackOptionResponse>

    @POST("auth/v1.0/otp/whatsapp/code")
    suspend fun requestWhatsappCode(@Body req: WhatsAppCodeRequest): Response<WhatsAppCodeResponse>

    @POST("auth/v1.0/otp:verify")
    suspend fun verifyOtp(
        @Body req: VerifyOtpRequest,
        @Header(OKC_LOGIN_FLOW_ID) flowId: String,
    ): Response<VerifyOtpResponse>

    @GET("auth/v1.0/otp/{otp_id}/status")
    suspend fun checkOtpStatus(
        @Path("otp_id") otp_id: String,
        @Query("otp_key") otp_key: String,
    ): Response<CheckOtpStatusResponse>

    companion object {
        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
        const val GRANT_TYPE_OTP = "otp"
        const val GRANT_TYPE_TRUECALLER = "truecaller"
        const val GRANT_TYPE_TRUECALLER_CALL = "truecaller_call"

        const val OTP_MODE_PUSH = "PUSH"
        const val OTP_MODE_PULL = "PULL"

        // by default it is kept as 10 seconds
        const val OTP_RETRY_TIME = 10

        // process of entering OTP should reset after 300 seconds, if the user is on the OTP screen
        const val OTP_FLOW_EXPIRY_TIME = 300

        const val OKC_LOGIN_FLOW_ID = "okc-login-flow-id"
    }

    enum class RetryDestination(val key: Int) {
        PRIMARY(0),
        SECONDARY(1),
        ;

        companion object {
            fun getDestination(code: Int?) = when (code) {
                SECONDARY.key -> SECONDARY
                else -> PRIMARY
            }
        }
    }

    enum class RequestOtpMedium(val key: Int) {
        SMS(0),
        WHATSAPP(1),
        CALL(2),
        ;

        companion object {
            fun getMedium(code: Int?) = when (code) {
                WHATSAPP.key -> WHATSAPP
                CALL.key -> CALL
                else -> SMS
            }
        }
    }
}

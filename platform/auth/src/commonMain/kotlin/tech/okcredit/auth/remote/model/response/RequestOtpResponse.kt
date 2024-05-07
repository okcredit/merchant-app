package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.okcredit.auth.remote.AuthApiClient.Companion.OTP_RETRY_TIME

@Serializable
data class RequestOtpResponse(
    @SerialName("otp_id")
    val otp_id: String,
    @SerialName("otp")
    val otp: String?,
    @SerialName("otp_key")
    val otp_key: String?,
    @SerialName("otp_flow_timeout")
    val otp_flow_timeout: Int?,
    @SerialName("fallback_options_show_time")
    val fallback_options_show_time: Int? = OTP_RETRY_TIME,
)

package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.okcredit.auth.remote.AuthApiClient

@Serializable
data class ResendOtpRequest(
    @SerialName("intent")
    val intent: Int,
    @SerialName("otp_id")
    val otp_id: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("language")
    val language: String,
    @SerialName("destination")
    val destination: Int = AuthApiClient.RetryDestination.PRIMARY.key,
    @SerialName("mixpanel_distinct_id")
    val mixpanel_distinct_id: String,
    @SerialName("request_origin")
    val request_origin: Int = 0,
    @SerialName("otp_flow_type")
    val otp_flow_type: String,
)

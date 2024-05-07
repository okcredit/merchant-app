package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("mode")
    val mode: String,
    @SerialName("otp_id")
    val otp_id: String,
    @SerialName("otp")
    val otp: String,
    @SerialName("otp_flow_type")
    val otp_flow_type: String,
)

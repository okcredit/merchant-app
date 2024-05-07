package tech.okcredit.auth.remote.model.request
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestOtpRequest(
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("mode")
    val mode: String,
    @SerialName("otp_flow_type")
    val otpFlowType: String,
)

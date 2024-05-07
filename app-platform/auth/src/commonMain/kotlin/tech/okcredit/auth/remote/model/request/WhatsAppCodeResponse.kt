package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WhatsAppCodeResponse(
    @SerialName("otp_id")
    val otp_id: String,
    @SerialName("otp_key")
    val otp_key: String,
    @SerialName("otp_code")
    val otp_code: String,
)

package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WhatsAppCodeRequest(
    @SerialName("distinct_id")
    val distinct_id: String?,
    @SerialName("redirect_url")
    val redirect_url: String,
    @SerialName("lang")
    val lang: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("purpose")
    val purpose: String,
)

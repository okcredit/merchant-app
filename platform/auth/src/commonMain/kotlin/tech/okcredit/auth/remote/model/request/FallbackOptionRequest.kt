package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FallbackOptionRequest(
    @SerialName("mixpanel_distinct_id")
    val mixpanel_distinct_id: String,
    @SerialName("mobile")
    val mobile: String,
    @SerialName("language")
    val language: String,
)

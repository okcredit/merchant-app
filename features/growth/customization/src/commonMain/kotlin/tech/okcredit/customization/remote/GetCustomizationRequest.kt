package tech.okcredit.customization.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCustomizationRequest(
    @SerialName(value = "version_code")
    val versionCode: String,
    @SerialName(value = "lang")
    val lang: String,
)

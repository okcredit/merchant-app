package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Address(
    @SerialName("text")
    val text: String?,
    @SerialName("latitude")
    val latitude: Double?,
    @SerialName("longitude")
    val longitude: Double?,
)

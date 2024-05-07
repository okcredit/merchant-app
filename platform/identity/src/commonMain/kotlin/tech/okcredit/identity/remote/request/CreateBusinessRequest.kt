package tech.okcredit.identity.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CreateBusinessRequest(
    @SerialName("name")
    val name: String,
)

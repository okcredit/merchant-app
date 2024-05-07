package tech.okcredit.okdoc.remote

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class OkDocApiResponse(
    @SerialName("request_id")
    val id: String,
    @SerialName("url")
    val remoteUrl: String,
)

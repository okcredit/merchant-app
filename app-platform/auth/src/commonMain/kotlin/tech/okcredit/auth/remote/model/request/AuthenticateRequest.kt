package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateRequest(
    @SerialName("grant_type")
    val grant_type: String,
    @SerialName("refresh_token")
    val refresh_token: String? = null,
    @SerialName("assertion")
    val assertion: String? = null,
    @SerialName("origin")
    val origin: Int,
    @SerialName("version")
    val version: Int,
)

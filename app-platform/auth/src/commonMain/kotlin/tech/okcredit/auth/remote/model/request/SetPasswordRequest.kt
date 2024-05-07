package tech.okcredit.auth.remote.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SetPasswordRequest(
    @SerialName("password")
    val password: String,
)

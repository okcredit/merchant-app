package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckPasswordSetResponse(
    val password_set: Boolean,
    val checksum: String?,
)

package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CheckOtpStatusResponse(
    val status: Int,
    val token: String?,
    val mobile: String?,
)

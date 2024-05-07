package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ResendOtpResponse(
    val retry_option_timeout: Int?,
)

package tech.okcredit.auth.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class FallbackOptionResponse(
    val retry_options: ArrayList<FallbackOption>,
)

@Serializable
data class FallbackOption(
    // It will be one of the values from RetryDestination
    val destination: Int,
    // It will consist of the values from RequestOtpMedium
    val intents: ArrayList<Int>,

    val data: FallbackOptionData?,
)

@Serializable
data class FallbackOptionData(
    val masked_alternate_number: String?,
)

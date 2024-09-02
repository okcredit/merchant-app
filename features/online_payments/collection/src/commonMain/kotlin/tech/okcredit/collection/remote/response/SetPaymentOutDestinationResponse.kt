package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SetPaymentOutDestinationResponse(
    @SerialName("success")
    val success: Boolean,
)

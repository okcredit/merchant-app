package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionStatusResponse(
    @SerialName("status")
    val status: String,
)

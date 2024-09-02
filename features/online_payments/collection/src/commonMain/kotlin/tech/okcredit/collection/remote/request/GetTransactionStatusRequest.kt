package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionStatusRequest(
    @SerialName("transaction_id")
    val transactionId: String,
    @SerialName("type")
    val type: String,
)

package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionAmountHistoryRequest(
    @SerialName("transaction_id")
    val transactionId: String,
)

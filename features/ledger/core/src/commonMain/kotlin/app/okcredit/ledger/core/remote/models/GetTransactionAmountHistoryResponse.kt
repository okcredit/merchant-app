package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class GetTransactionAmountHistoryResponse(
    @SerialName(value = "transaction")
    var transaction: TransactionAmountHistory,
)

@kotlinx.serialization.Serializable
data class TransactionAmountHistory(
    @SerialName(value = "transaction_id")
    val transactionId: String,

    @SerialName(value = "amount")
    var amount: String? = null,

    @SerialName(value = "amount_updated")
    var amountUpdated: Boolean? = null,

    @SerialName(value = "amount_updated_at")
    var amountUpdatedAt: String? = null,

    @SerialName(value = "initial")
    var initial: Initial? = null,

    @SerialName(value = "history")
    var history: List<History>? = null,
)

@kotlinx.serialization.Serializable
class Initial(
    @SerialName(value = "amount")
    var amount: String? = null,

    @SerialName(value = "created_at")
    var createdAt: String? = null,
)

@kotlinx.serialization.Serializable
class History {
    @SerialName(value = "old_amount")
    var oldAmount: String? = null

    @SerialName(value = "new_amount")
    var newAmount: String? = null

    @SerialName(value = "created_at")
    var createdAt: String? = null
}

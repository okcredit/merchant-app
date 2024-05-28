package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetFileIdForSyncTransactionsRequest(
    // 0 for customer and 1 for supplier
    @SerialName(value = "account_type")
    val accountType: Int,
)

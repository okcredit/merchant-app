package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionFileResponse(
    @SerialName("txn_file")
    val txnFile: TransactionFile,
)

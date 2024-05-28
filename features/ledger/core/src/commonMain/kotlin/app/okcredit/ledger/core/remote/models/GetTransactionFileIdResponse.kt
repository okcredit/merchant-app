package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class GetTransactionFileIdResponse(
    @SerialName(value = "transaction_file_id")
    val transactionFileId: String,
)

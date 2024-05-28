package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionFileRequest(
    @SerialName("id")
    val id: String,
)

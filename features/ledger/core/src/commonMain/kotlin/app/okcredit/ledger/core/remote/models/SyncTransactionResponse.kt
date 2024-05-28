package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SyncTransactionResponse(
    @SerialName("operation_responses")
    val operationResponses: List<OperationResponseForTransactions>,
)

@kotlinx.serialization.Serializable
data class OperationResponseForTransactions(
    @SerialName("id")
    val id: String,
    @SerialName("status")
    val status: Int,
    @SerialName("error")
    val error: Error? = null,
)

@kotlinx.serialization.Serializable
data class Error(
    @SerialName("code")
    val code: Int,
    @SerialName("description")
    val description: String?,
)

enum class Status(val value: Int) {
    FAILURE(0),
    SUCCESS(1),
}

enum class ErrorCodes(val value: Int) {
    CONFLICT(409),
    INTERNAL(500),
    DEPENDENCY(600),
}

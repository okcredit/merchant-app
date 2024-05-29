package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SyncTransactionRequest(
    @SerialName("operations")
    val operations: List<ApiTransactionCommand>,
)

@kotlinx.serialization.Serializable
data class ApiTransactionCommand(
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: Int,
    @SerialName("path")
    val path: String,
    @SerialName("transaction")
    val transaction: ApiTransaction? = null,
    @SerialName("image")
    val image: ApiTransaction.TransactionImage? = null,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("mask")
    val mask: List<String>? = null,
    @SerialName("transaction_id")
    val transactionId: String,
    @SerialName("image_id")
    val imageId: String? = null,
)

enum class ApiCommandType(val type: Type, val path: String, val mask: List<String>?) {
    UNKNOWN(Type.UNKNOWN, "", null),
    CREATE_TRANSACTION(Type.ADD, Path.TRANSACTION.value, null),
    UPDATE_TRANSACTION_NOTE(Type.UPDATE, Path.TRANSACTION.value, listOf("note")),
    UPDATE_TRANSACTION_AMOUNT(Type.UPDATE, Path.TRANSACTION.value, listOf("amount")),
    DELETE_TRANSACTION(Type.UPDATE, Path.TRANSACTION.value, listOf("transaction_state", "deleter_role")),
    CREATE_TRANSACTION_IMAGE(Type.ADD, Path.IMAGES.value, null),
    DELETE_TRANSACTION_IMAGE(Type.DELETE, Path.IMAGES.value, null),
    ;

    enum class Type(val value: Int) {
        UNKNOWN(0),
        ADD(1),
        UPDATE(2),
        DELETE(3),
    }

    enum class Path(val value: String) {
        TRANSACTION("/transactions"),
        IMAGES("/images"),
    }
}

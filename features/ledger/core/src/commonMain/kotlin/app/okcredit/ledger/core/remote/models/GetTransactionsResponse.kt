package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetTransactionsResponse(
    // type of transactions (LIST = 0, FILE = 1)
    @SerialName("type")
    val type: Int,
    // transactions info if type is list.
    @SerialName("list_data")
    val listData: ListData?,
    // denotes role of a merchant in an account (UNKNOWN = 0, SELLER = 1, BUYER = 2)
    @SerialName("file_data")
    val fileData: FileData?,
)

@kotlinx.serialization.Serializable
data class ListData(
    @SerialName("transactions")
    val transactions: List<ApiTransaction>,
)

@kotlinx.serialization.Serializable
data class FileData(
    @SerialName("txn_file")
    val txnFile: TransactionFile,
)

@kotlinx.serialization.Serializable
data class TransactionFile(
    @SerialName("id")
    val id: String,
    // denotes file creation status (FILE_DOWNLOAD_IN_PROGRESS = 0, FILE_DOWNLOAD_COMPLETED = 1)
    @SerialName("status")
    val status: Int?,
    @SerialName("encryption_key")
    val encryptionKey: String?,
    @SerialName("file")
    val file: String?,
)

const val TYPE_LIST = 0
const val TYPE_FILE = 1

const val ROLE_UNKNOWN = 0
const val ROLE_SELLER = 1 // Suppliers
const val ROLE_BUYER = 2 // Customers

const val FILE_CREATION_IN_PROGRESS = 0
const val FILE_CREATION_COMPLETED = 1

//
// @kotlinx.serialization.Serializable
// data class SupplierTransactionResponse(
//    @SerialName("transactions")
//    val transactions: List<SupplierApiTransaction>
// )
//
// @kotlinx.serialization.Serializable
// data class SupplierApiTransaction(
//    val id: String,
//    val supplier_id: String,
//    val collection_id: String?,
//    val payment: Boolean,
//    val amount: Long,
//    val note: String?,
//    val receipt_url: String?,
//    val bill_date: DateTime,
//    val create_time: DateTime,
//    val created_by_supplier: Boolean,
//    val deleted: Boolean,
//    val delete_time: DateTime?,
//    val deleted_by_supplier: Boolean,
//    val update_time: DateTime,
//    val transaction_state: Int,
//    val tx_category: Int?,
//    val reference_id: String?,
//    val reference_source: Int?,
// )

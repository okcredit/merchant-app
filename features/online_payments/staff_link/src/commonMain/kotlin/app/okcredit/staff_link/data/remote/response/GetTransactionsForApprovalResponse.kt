package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTransactionsForApprovalResponse(
    @SerialName("transactions")
    val transactions: List<PendingTransaction>
)

@Serializable
data class PendingTransaction(
    @SerialName(value = "account_txn_id")
    val accountTxnId: String,
    @SerialName(value = "advance_amount")
    val advanceAmount: Long,
    @SerialName(value = "amount")
    val amount: Long,
    @SerialName(value = "approved_by_merchant")
    val approvedByMerchant: Boolean,
    @SerialName(value = "associated_account_id")
    val associatedAccountId: String,
    @SerialName(value = "business_id")
    val businessId: String,
    @SerialName(value = "collection_list_id")
    val collectionListId: String,
    @SerialName(value = "created_by")
    val createdBy: Int,
    @SerialName(value = "delete_time")
    val deleteTime: Long,
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "is_bill_settled")
    val isBillSettled: Boolean,
    @SerialName(value = "is_deleted")
    val isDeleted: Boolean,
    @SerialName(value = "notes")
    val notes: String,
    @SerialName(value = "settlement_bill_ids")
    val settlementBillIds: List<String>,
    @SerialName(value = "staff_id")
    val staffId: String,
    @SerialName(value = "staff_mobile")
    val staffMobile: String,
    @SerialName(value = "staff_name")
    val staffName: String,
    @SerialName(value = "transaction_time")
    val transactionTime: Long,
    @SerialName(value = "txn_type")
    val txnType: Int
)

enum class TransactionType(val type: Int) {
    NA(0),
    CASH(1),
    ONLINE(2),
    CREDIT_DUES(3),
    CHEQUE(4),
    NEFT(5),
    RETURN(6),
    DAMAGED(7);

    companion object {
        val map = values().associateBy(TransactionType::type)
        fun fromValue(value: Int?) = map[value] ?: NA
    }
}

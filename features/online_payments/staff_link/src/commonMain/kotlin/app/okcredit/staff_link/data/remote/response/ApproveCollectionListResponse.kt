package merchant.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApproveCollectionListResponse(
    @SerialName(value = "transactions")
    val transactions: List<Transaction>
) {
    @Serializable
    data class Transaction(
        @SerialName(value = "account_txn_id")
        val accountTxnId: String,
        @SerialName(value = "add_as_advance")
        val addAsAdvance: Boolean,
        @SerialName(value = "added_as_advance")
        val addedAsAdvance: Boolean,
        @SerialName(value = "advance_amount")
        val advanceAmount: String,
        @SerialName(value = "amount")
        val amount: String,
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
        val deleteTime: String,
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
        val transactionTime: String,
        @SerialName(value = "txn_type")
        val txnType: Int
    )
}

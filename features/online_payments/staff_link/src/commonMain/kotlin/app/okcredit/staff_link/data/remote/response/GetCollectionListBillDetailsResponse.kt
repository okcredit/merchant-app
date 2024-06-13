package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionListBillDetailsResponse(
    @SerialName(value = "account_details")
    val accountDetails: List<AccountDetail>,
    @SerialName(value = "bill_details")
    val billDetails: List<BillDetails>,
    @SerialName(value = "collection_list")
    val collectionList: CollectionList,
    @SerialName(value = "merchant_mobile")
    val merchantMobile: String,
    @SerialName(value = "merchant_name")
    val merchantName: String,
) {
    @Serializable
    data class AccountDetail(
        @SerialName(value = "address")
        val address: String?,
        @SerialName(value = "id")
        val id: String,
        @SerialName(value = "mobile")
        val mobile: String?,
        @SerialName(value = "name")
        val name: String
    )

    @Serializable
    data class BillDetails(
        @SerialName(value = "approved_transactions_summary")
        val approvedTransactionsSummary: ApprovedTransactionsSummary?,
        @SerialName(value = "bill")
        val bill: BillDetail,
        @SerialName(value = "transactions")
        val transactions: List<PendingTransaction>?
    ) {
        @Serializable
        data class ApprovedTransactionsSummary(
            @SerialName(value = "total_cash_or_neft_payments")
            val totalCashOrNeftPayments: String,
            @SerialName(value = "total_cash_payments")
            val totalCashPayments: String,
            @SerialName(value = "total_cheque_payments")
            val totalChequePayments: String,
            @SerialName(value = "total_damaged_payments")
            val totalDamagedPayments: String,
            @SerialName(value = "total_neft_payments")
            val totalNeftPayments: String,
            @SerialName(value = "total_online_payments")
            val totalOnlinePayments: String,
            @SerialName(value = "total_return_payments")
            val totalReturnPayments: String,
            @SerialName(value = "total_wallet_payments")
            val totalWalletPayments: String
        )
    }

    @Serializable
    data class CollectionList(
        @SerialName(value = "business_id")
        val businessId: String,
        @SerialName(value = "create_time")
        val createTime: String,
        @SerialName(value = "due_config")
        val dueConfig: DueConfig,
        @SerialName(value = "id")
        val id: String,
        @SerialName(value = "is_active")
        val isActive: Boolean,
        @SerialName(value = "name")
        val name: String,
        @SerialName(value = "type")
        val type: Int,
        @SerialName(value = "update_time")
        val updateTime: String,
        @SerialName(value = "url")
        val url: String,
        @SerialName(value = "usage_type")
        val usageType: Int
    )
}

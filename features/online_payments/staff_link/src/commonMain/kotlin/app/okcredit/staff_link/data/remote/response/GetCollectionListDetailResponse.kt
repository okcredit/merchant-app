package merchant.okcredit.staff_link.data.remote.response

import app.okcredit.staff_link.data.remote.response.BillDetail
import app.okcredit.staff_link.data.remote.response.DueConfig
import app.okcredit.staff_link.data.remote.response.PendingTransaction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionListDetailResponse(
    @SerialName(value = "account_details")
    val accountDetails: List<AccountDetail>,
    @SerialName(value = "collection_list")
    val collectionList: CollectionList,
    @SerialName(value = "merchant_mobile")
    val merchantMobile: String,
    @SerialName(value = "merchant_name")
    val merchantName: String,
) {
    @Serializable
    data class AccountDetail(
        @SerialName(value = "account_id")
        val accountId: String,
        @SerialName(value = "address")
        val address: String,
        @SerialName(value = "balance")
        val balance: Long,
        @SerialName(value = "bills")
        val bills: List<Bill>,
        @SerialName(value = "customer_mobile")
        val customerMobile: String,
        @SerialName(value = "customer_name")
        val customerName: String,
        @SerialName(value = "profile")
        val profile: Profile,
        @SerialName(value = "registration")
        val registration: String?,
        @SerialName(value = "transactions")
        val transactions: List<PendingTransaction>?
    ) {
        @Serializable
        data class Bill(
            @SerialName(value = "bill")
            val bill: BillDetail,
        )

        @Serializable
        data class Profile(
            @SerialName(value = "message")
            val message: String,
            @SerialName(value = "message_link")
            val messageLink: String,
            @SerialName(value = "qr_intent")
            val qrIntent: String
        )
    }

    @Serializable
    data class CollectionList(
        @SerialName(value = "associated_account_ids")
        val associatedAccountIds: List<String>,
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
        val url: String
    )
}

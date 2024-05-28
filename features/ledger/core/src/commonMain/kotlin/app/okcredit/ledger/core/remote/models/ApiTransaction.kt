package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApiTransaction(
    @SerialName("id")
    val id: String? = null,
    @SerialName("account_id")
    val accountId: String? = null,
    @SerialName("type")
    val type: Int? = null,
    @SerialName("amount")
    val amount: Long? = null,
    @SerialName("creator_role")
    val creatorRole: Int? = null,
    @SerialName("create_time")
    val createTime: Long? = null,
    @SerialName("deleted")
    val deleted: Boolean? = null,
    @SerialName("deleter_role")
    val deleterRole: Int? = null,
    @SerialName("delete_time")
    val deleteTime: Long? = null,
    @SerialName("note")
    val note: String? = null,
    @SerialName("images")
    val images: List<TransactionImage>? = null,
    @SerialName("bill_date")
    val billDate: Long? = null,
    @SerialName("alert_sent_by_creator")
    val alertSentByCreator: Boolean? = null,
    @SerialName("collection_id")
    val collectionId: String? = null,
    @SerialName("update_time")
    val updateTime: Long? = null,
    @SerialName("receipt_url")
    val receiptUrl: String? = null,
    @SerialName("meta")
    val meta: Meta? = null,
    @SerialName("transaction_state")
    val transactionState: Int? = null,
    @SerialName("tx_category")
    val txCategory: Int? = null,
    @SerialName("create_time_ms")
    val createTimeMs: Long? = null,
    @SerialName("delete_time_ms")
    val deleteTimeMs: Long? = null,
    @SerialName("bill_date_ms")
    val billDateMs: Long? = null,
    @SerialName("update_time_ms")
    val updateTimeMs: Long? = null,
    @SerialName("amount_updated")
    val amountUpdated: Boolean? = null,
    @SerialName("amount_updated_at")
    val amountUpdatedAt: Long? = null,
    @SerialName("reference_id")
    val referenceId: String? = null,
    @SerialName("reference_source")
    val referenceSource: Int? = null,
) {

    @kotlinx.serialization.Serializable
    data class TransactionImage(
        @SerialName("id")
        val id: String? = null,
        @SerialName("transaction_id")
        val transactionId: String? = null,
        @SerialName("url")
        val url: String,
        @SerialName("create_time")
        val createTime: Long,
    )

    @kotlinx.serialization.Serializable
    data class Meta(
        @SerialName("intent")
        val intent: String? = null,
        @SerialName("intent_id")
        val intentId: String? = null,
    )
}

package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ListSendMoneyTransactionsRequest(
    @SerialName("update_time")
    val updateTime: Long? = null,
    @SerialName("business_id")
    val businessId: String,
)

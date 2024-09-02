package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ListSendMoneyTransactionsResponse(
    @SerialName("transactions")
    val transactions: List<CollectionOnlinePaymentApi>,
)

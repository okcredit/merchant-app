package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetOnlinePaymentResponse(
    @SerialName("merchantCollections")
    val onlinePayments: List<CollectionOnlinePaymentApi>,
)

package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TagMerchantPaymentRequest(
    @SerialName("account_id")
    val accountId: String,
    @SerialName("collection_id")
    val paymentId: String,
)
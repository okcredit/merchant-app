package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PaymentTagRequest(
    @SerialName("payment_id")
    val paymentId: String? = null,
    @SerialName("payment_type")
    val paymentType: String? = null,
    @SerialName("tags")
    val tags: TagRequest,
    @SerialName("timestamp")
    val timestamp: Long? = null,
)

@kotlinx.serialization.Serializable
data class TagRequest(
    @SerialName("isViewed")
    val isViewed: String,
)

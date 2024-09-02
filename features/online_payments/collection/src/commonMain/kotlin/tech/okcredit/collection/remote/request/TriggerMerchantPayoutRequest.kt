package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class TriggerMerchantPayoutRequest(
    @SerialName("payment_id")
    val paymentId: List<String>,
)

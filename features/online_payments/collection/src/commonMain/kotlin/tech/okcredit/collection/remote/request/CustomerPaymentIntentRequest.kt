package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CustomerPaymentIntentRequest(
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("customer_id")
    val customerId: String,
    @SerialName("payment_intent")
    val paymentIntent: String,
)

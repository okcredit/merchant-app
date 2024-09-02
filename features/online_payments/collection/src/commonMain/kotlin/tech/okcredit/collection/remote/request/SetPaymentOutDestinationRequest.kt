package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SetPaymentOutDestinationRequest(
    @SerialName("account_id")
    val account_id: String,

    @SerialName("destination")
    val destination: PaymentOutDestination,

    @SerialName("account_type")
    val accountType: String,
)

@kotlinx.serialization.Serializable
data class PaymentOutDestination(
    @SerialName("type")
    val type: String,

    @SerialName("payment_address")
    val paymentAddress: String,
)

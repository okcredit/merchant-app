package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ValidatePaymentAddressRequest(
    @SerialName("payment_address_type")
    val payment_address_type: String,

    @SerialName("payment_address")
    val payment_address: String,
)

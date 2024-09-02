package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ValidatePaymentAddressResponse(
    @SerialName("valid")
    val valid: Boolean,

    @SerialName("name")
    val name: String?,
)

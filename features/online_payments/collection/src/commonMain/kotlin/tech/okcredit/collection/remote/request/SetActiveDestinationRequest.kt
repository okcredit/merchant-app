package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SetActiveDestinationRequest(
    @SerialName("merchant_id")
    val merchant_id: String,

    @SerialName("destination")
    val destination: DestinationRequest,

    @SerialName("async")
    val async: Boolean = false,

    @SerialName("referral_merchant")
    val referralMerchant: String = "",
)

@kotlinx.serialization.Serializable
data class DestinationRequest(
    @SerialName("mobile")
    val mobile: String? = "",

    @SerialName("name")
    val name: String? = "",

    @SerialName("payment_address")
    val paymentAddress: String,

    @SerialName("type")
    val type: String,

    @SerialName("upi_vpa")
    val upiVpa: String? = "",
)

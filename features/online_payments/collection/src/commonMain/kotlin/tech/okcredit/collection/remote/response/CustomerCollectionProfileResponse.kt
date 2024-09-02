package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CustomerCollectionProfileResponse(
    // this could be null in case when profile is not present and we receive error from server
    @SerialName("customer_id")
    val customerId: String?,

    @SerialName("profile")
    val profile: Profile?,

    @SerialName("destination")
    val destination: Destination?,

    @SerialName("cashback_eligible")
    val cashbackEligible: Boolean?,
)

@kotlinx.serialization.Serializable
data class Profile(
    @SerialName("customer_id")
    val customerId: String,

    @SerialName("message_link")
    val messageLink: String?,

    @SerialName("message")
    val message: String?,

    @SerialName("link_intent")
    val linkIntent: String?,

    @SerialName("qr_intent")
    val qrIntent: String?,

    @SerialName("show_image")
    val showImage: Boolean,

    @SerialName("from_merchant_payment_link")
    val fromMerchantPaymentLink: String?,

    @SerialName("from_merchant_upi_intent")
    val fromMerchantUpiIntent: String?,

    @SerialName("link_id")
    val linkId: String?,

    @SerialName("payment_intent")
    val paymentIntent: Boolean,
)

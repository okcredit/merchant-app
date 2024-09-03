package tech.okcredit.collection.model

data class CollectionCustomerProfile(
    val accountId: String,
    val message_link: String? = "",
    val link_intent: String? = "",
    val qr_intent: String? = "",
    val isSupplier: Boolean = false,
    val name: String? = "",
    val mobile: String? = "",
    val linkVpa: String? = "",
    val type: String? = "",
    val paymentAddress: String? = "",
    val upiVpa: String? = "",
    val fromMerchantPaymentLink: String? = "",
    val fromMerchantUpiIntent: String? = "",
    val linkId: String? = "",
    val destinationUpdateAllowed: Boolean = true,
)

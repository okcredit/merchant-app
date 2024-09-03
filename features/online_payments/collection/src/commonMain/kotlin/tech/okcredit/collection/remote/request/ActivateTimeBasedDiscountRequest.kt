package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ActivateTimeBasedDiscountRequest(
    @SerialName("link_id")
    val linkId: String,

    @SerialName("discount")
    val discount: ActivateTimeBasedDiscountDetails,
)

@kotlinx.serialization.Serializable
data class ActivateTimeBasedDiscountDetails(
    @SerialName("discount_percentage")
    val discountPercentage: String,

    @SerialName("valid_upto")
    val validUpto: Long,
)

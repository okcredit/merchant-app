package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetBusinessResponse(
    @SerialName("business_user")
    val business_user: GetBusinessResponseWrapper,
)

@kotlinx.serialization.Serializable
data class GetBusinessResponseWrapper(
    @SerialName("user")
    val user: BusinessUser,
    @SerialName("business_category")
    var business_category: Category? = null,
    @SerialName("business_type")
    var business_type: BusinessType? = null,
    @SerialName("is_first")
    val is_first: Boolean? = null,
    @SerialName("contact_name")
    val contact_name: String? = null,
)

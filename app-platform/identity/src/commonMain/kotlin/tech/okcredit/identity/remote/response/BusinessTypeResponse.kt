package tech.okcredit.identity.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class BusinessTypeResponse(
    @SerialName("business_type")
    val business_type: List<BusinessType>,
)

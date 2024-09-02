package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetRiskAttributesRequest(
    @SerialName("user_id")
    val merchantId: String,
    @SerialName("user_type")
    val userType: String = "MERCHANT",
    @SerialName("service_name")
    val serviceName: String = "collection",
)

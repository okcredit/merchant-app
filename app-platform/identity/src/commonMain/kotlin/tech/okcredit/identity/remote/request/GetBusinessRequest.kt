package tech.okcredit.identity.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetBusinessRequest(
    @SerialName("business_user_id")
    val businessId: String,
)

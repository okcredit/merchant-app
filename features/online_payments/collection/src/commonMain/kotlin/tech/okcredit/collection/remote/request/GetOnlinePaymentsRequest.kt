package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetOnlinePaymentsRequest(
    @SerialName("start_time")
    val startTime: Long? = null,
    @SerialName("business_id")
    val business_id: String,
)

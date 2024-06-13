package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActiveSubscriptionRequest(
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("plan_name")
    val planName: String? = null
)

package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSubscriptionRequest(
    @SerialName(value = "merchant_id")
    val merchantId: String,
    @SerialName(value = "plan_id")
    val planId: String
)

package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetActiveSubscriptionResponse(
    @SerialName(value = "subscription")
    val subscription: Subscription?
)

@Serializable
data class Subscription(
    @SerialName(value = "current_end")
    val currentEnd: String?,
    @SerialName(value = "current_start")
    val currentStart: String?,
    @SerialName(value = "expiration_date")
    val expirationDate: String?,
    @SerialName(value = "is_trial")
    val isTrial: String,
    @SerialName(value = "link_id")
    val linkId: String,
    @SerialName(value = "merchant_id")
    val merchantId: String,
    @SerialName(value = "plan")
    val plan: Plan?,
    @SerialName(value = "status")
    val status: String?,
    @SerialName(value = "subscription_id")
    val subscriptionId: String,
)

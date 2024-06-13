package app.okcredit.staff_link.data.remote.response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSubscriptionPlansResponse(
    @SerialName(value = "plans")
    val plans: List<Plan>
)

@Serializable
data class Plan(
    @SerialName(value = "active")
    val active: Boolean,
    @SerialName(value = "currency")
    val currency: String,
    @SerialName(value = "description")
    val description: String,
    @SerialName(value = "discount")
    val discount: String,
    @SerialName(value = "plan_id")
    val planId: String,
    @SerialName(value = "plan_name")
    val planName: String,
    @SerialName(value = "price")
    val price: Long,
    @SerialName(value = "price_frequency")
    val priceFrequency: String?,
    @SerialName(value = "product_pricing")
    val productPricing: ProductPricing?,
    @SerialName(value = "trial_days")
    val trialDays: String?
)

@Serializable
data class ProductPricing(
    @SerialName(value = "instruments")
    val instruments: Instruments?,
    @SerialName(value = "product_discount")
    val productDiscount: String?
)

@Serializable
data class Instruments(
    @SerialName(value = "credit_card")
    val creditCard: String,
    @SerialName(value = "debit_card")
    val debitCard: String,
    @SerialName(value = "net_banking")
    val netBanking: String,
    @SerialName(value = "upi")
    val upi: String,
    @SerialName(value = "wallet")
    val wallet: String
)

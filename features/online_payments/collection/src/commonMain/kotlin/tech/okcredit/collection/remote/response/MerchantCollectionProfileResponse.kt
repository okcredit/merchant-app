package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MerchantCollectionProfileResponse(
    @SerialName("business_id")
    val businessId: String?,
    @SerialName("destination")
    val destination: Destination?,
    @SerialName("kyc_status")
    val kycStatus: String?,
    @SerialName("limit")
    val limit: String?,
    @SerialName("limit_type")
    val limitType: String?,
    @SerialName("merchant_link")
    val merchantLink: String?,
    @SerialName("merchant_qr_enabled")
    val merchantQrEnabled: Boolean?,
    @SerialName("merchant_vpa")
    val merchantVpa: String?,
    @SerialName("payment_provider")
    val paymentProvider: String?,
    @SerialName("qr_intent")
    val qrIntent: String?,
    @SerialName("remaining_limit")
    val remainingLimit: String?,
    @SerialName("risk_category")
    val riskCategory: String?,
)

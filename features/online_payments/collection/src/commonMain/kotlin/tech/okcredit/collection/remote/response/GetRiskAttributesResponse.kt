package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetRiskAttributesResponse(
    @SerialName("kyc_info")
    val kycInfo: KycInfo,
    @SerialName("risk_category")
    val riskCategory: String,
    @SerialName("limit_info")
    val limitInfo: LimitInfo,
)

@kotlinx.serialization.Serializable
data class LimitInfo(
    @SerialName("upi_limit_info")
    val upiLimit: Limit,
    @SerialName("non_upi_limit_info")
    val nonUpiLimit: Limit,
)

@kotlinx.serialization.Serializable
data class Limit(
    @SerialName("daily_limit_reached")
    val dailyLimitReached: Boolean,
    @SerialName("total_daily_amount_limit")
    val totalDailyAmountLimit: Long,
    @SerialName("total_daily_limit_used")
    val totalDailyLimitUsed: Long,
    @SerialName("total_daily_transaction_limit")
    val totalDailyTransactionLimit: Long,
    @SerialName("remaining_daily_amount_limit")
    val remainingDailyAmountLimit: Long,
    @SerialName("remaining_daily_transaction_limit")
    val remainingDailyTransactionLimit: Long,
)

@kotlinx.serialization.Serializable
data class KycInfo(
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_type")
    val userType: String,
    @SerialName("pan_verified")
    val panVerified: Boolean,
    @SerialName("aadhaar_verified")
    val aadhaarVerified: Boolean,
    @SerialName("service_name")
    val serviceName: String,
    @SerialName("kyc_status")
    val kycStatus: String,
    @SerialName("address")
    val address: String,
    @SerialName("address_type")
    val addressType: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
)

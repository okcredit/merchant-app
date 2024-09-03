package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class CollectionOnlinePaymentApi(
    @SerialName("id")
    val id: String,
    @SerialName("create_time")
    val createdTime: Long,
    @SerialName("update_time")
    val updatedTime: Long,
    @SerialName("status")
    val status: Int,
    @SerialName("merchant_id")
    val merchantId: String,
    @SerialName("account_id")
    val accountId: String? = "",
    @SerialName("amount")
    val amount: Double,
    @SerialName("payment_id")
    val paymentId: String? = "",
    @SerialName("payout_id")
    val payoutId: String? = "",
    @SerialName("payment_source")
    val paymentSource: String? = "",
    @SerialName("payment_mode")
    val paymentMode: String? = "",
    @SerialName("type")
    val type: String,
    @SerialName("error_code")
    val errorCode: String? = "",
    @SerialName("error_description")
    val errorDescription: String? = "",
    @SerialName("surcharge")
    val surcharge: Long?,
    @SerialName("payment_from")
    val paymentFrom: String? = null,
    @SerialName("payment_utr")
    val paymentUtr: String? = null,
    @SerialName("payout_utr")
    val payoutUtr: String? = null,
    @SerialName("payment_destination")
    val paymentDestination: String?,
    @SerialName("payment_to")
    val paymentTo: String?,
    @SerialName("labels")
    val labels: Map<String, String>?,
)

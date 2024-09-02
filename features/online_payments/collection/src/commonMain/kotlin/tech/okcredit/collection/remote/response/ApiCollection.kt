package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiCollection(
    @SerialName("id")
    val id: String,

    @SerialName("create_time")
    val create_time: Long,

    @SerialName("update_time")
    val update_time: Long,

    @SerialName("labels")
    val labels: Map<String, String>? = null,

    @SerialName("status")
    val status: Int,

    @SerialName("payment_link")
    val payment_link: String? = null,

    @SerialName("amount_requested")
    val amount_requested: Long,

    @SerialName("amount_collected")
    val amount_collected: Long? = null,

    @SerialName("fee")
    val fee: Long? = null,

    @SerialName("expire_time")
    val expire_time: Long? = null,

    @SerialName("customer_id")
    val customer_id: String? = null,

    @SerialName("discount")
    val discount: Long? = null,

    @SerialName("fee_category")
    val fee_category: Int? = null,

    @SerialName("settlement_category")
    val settlement_category: Int? = null,

    @SerialName("merchantName")
    val merchantName: String? = null,

    @SerialName("events")
    val events: List<Event>,

    @SerialName("payment")
    val payment: Payment? = null,

    @SerialName("payout")
    val payout: Payout? = null,

    @SerialName("payment_id")
    val paymentId: String,

    @SerialName("error_code")
    val errorCode: String? = null,

    @SerialName("error_description")
    val errorDescription: String? = null,

    @SerialName("cashback_given")
    val cashbackGiven: Boolean? = null,

    @SerialName("merchant_cashback_given")
    val merchantCashbackGiven: Boolean? = null,

    @SerialName("payment_source")
    val paymentSource: String? = "",

    @SerialName("surcharge")
    val surcharge: Long? = null,

    @SerialName("payment_from")
    val paymentFrom: String? = null,

    @SerialName("payment_utr")
    val paymentUtr: String? = null,

    @SerialName("payout_utr")
    val payoutUtr: String? = null,

    @SerialName("merchant_id")
    val merchantId: String? = null,

    @SerialName("payment_destination")
    val paymentDestination: String? = null,

    @SerialName("payment_to")
    val paymentTo: String? = null,

    @SerialName("platform_fee")
    val platformFee: Long? = null,
)

@Serializable
data class Origin(
    @SerialName("mobile")
    val mobile: String? = null,

    @SerialName("name")
    val name: String? = null,

    @SerialName("payment_address")
    val payment_address: String? = null,

    @SerialName("type")
    val type: String? = null,
)

@Serializable
data class Payment(
    @SerialName("provider")
    val provider: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("UTR")
    val UTR: String? = null,

    @SerialName("reference")
    val reference: String? = null,

    @SerialName("status")
    val status: Int? = null,

    @SerialName("amount")
    val amount: Long? = null,

    @SerialName("fee")
    val fee: Long? = null,

    @SerialName("tax")
    val tax: Long? = null,

    @SerialName("payment_link")
    val payment_link: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("request_type")
    val request_type: Int? = null,

    @SerialName("labels")
    val labels: Map<String, String>,

    @SerialName("origin")
    val origin: Origin? = null,
)

@Serializable
data class Event(
    @SerialName("status")
    val status: Int? = null,

    @SerialName("timestamp")
    val timestamp: Long? = null,
)

@Serializable
data class Payout(
    @SerialName("provider")
    val provider: String? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("UTR")
    val UTR: String? = null,

    @SerialName("reference")
    val reference: String? = null,

    @SerialName("status")
    val status: Int? = null,

    @SerialName("amount")
    val amount: Long? = null,

    @SerialName("destination")
    val destination: Destination? = null,

    @SerialName("fee")
    val fee: Long? = null,

    @SerialName("labels")
    val labels: Map<String, String>,

    @SerialName("tax")
    val tax: Long? = null,
)

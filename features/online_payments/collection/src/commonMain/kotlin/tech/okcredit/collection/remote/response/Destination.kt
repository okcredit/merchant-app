package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Destination(
    @SerialName("email")
    val email: String?,
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("payment_address")
    val paymentAddress: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("upi_vpa")
    val upiVpa: String?,
)

package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SupplierCollectionProfileResponse(
    // this could be null in case when profile/destination is not present and we receive error from server
    @SerialName("account_id")
    val accountId: String?,

    @SerialName("profile")
    val supplierProfile: SupplierProfile?,

    @SerialName("destination")
    val destination: SupplierDestination?,

    @SerialName("destination_update_allowed")
    val destinationUpdateAllowed: Boolean?,
)

@kotlinx.serialization.Serializable
data class SupplierProfile(
    @SerialName("message_link")
    val messageLink: String?,

    @SerialName("link_intent")
    val linkIntent: String?,

    @SerialName("linkVpa")
    val linkVpa: String?,

    @SerialName("link_id")
    val linkId: String?,
)

@kotlinx.serialization.Serializable
data class SupplierDestination(
    @SerialName("mobile")
    val mobile: String? = "",

    @SerialName("name")
    val name: String? = "",

    @SerialName("payment_address")
    val paymentAddress: String?,

    @SerialName("type")
    val type: String?,

    @SerialName("upi_vpa")
    val upiVpa: String? = "",
)

package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddCustomerRequest(
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("description")
    val description: String,
    @SerialName("reactivate")
    val reactivate: Boolean,
    @SerialName("profile_image")
    val profileImage: String?,
)

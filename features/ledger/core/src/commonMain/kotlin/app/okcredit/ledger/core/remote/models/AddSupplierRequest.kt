package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

data class AddSupplierRequest(
    @SerialName("name")
    val name: String,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("mobile")
    val mobile: String?,
)

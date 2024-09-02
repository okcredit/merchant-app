package tech.okcredit.collection.remote.response

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SupplierCollectionProfilesResponse(
    @SerialName("suppliers")
    val supplierPaymentProfilesList: List<SupplierCollectionProfileResponse?>?,
)

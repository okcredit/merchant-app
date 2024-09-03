package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class GetSupplierCollectionProfileRequest(
    @SerialName("account_id")
    val accountId: String,
)

package tech.okcredit.collection.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomerCollectionProfileRequest(
    @SerialName("merchant_id")
    val merchant_id: String,

    @SerialName("customer_id")
    val customer_id: String,
)

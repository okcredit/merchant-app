package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionListDetailRequest(
    @SerialName(value = "business_id")
    val businessId: String,
    @SerialName(value = "collection_list_id")
    val collectionListId: String
)

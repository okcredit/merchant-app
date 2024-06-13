package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCollectionListBillDetailsRequest(
    @SerialName(value = "business_id")
    val businessId: String,
    @SerialName(value = "collection_list_id")
    val collectionListId: String,
    @SerialName(value = "version")
    val version: Int = 2
)

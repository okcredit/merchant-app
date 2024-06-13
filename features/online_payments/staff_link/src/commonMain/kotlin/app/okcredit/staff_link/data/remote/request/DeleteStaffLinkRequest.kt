package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class DeleteStaffLinkRequest(
    @SerialName(value = "collection_list")
    val collectionList: DeleteCollectionList
)

@Serializable
data class DeleteCollectionList(
    @SerialName(value = "id")
    val id: String
)

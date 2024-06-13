package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStaffLinkResponse(
    @SerialName(value = "collection_list")
    val collectionList: CollectionList
)

@Serializable
data class CollectionList(
    @SerialName(value = "associated_account_ids")
    val associatedAccountIds: List<String>,
    @SerialName(value = "business_id")
    val businessId: String,
    @SerialName(value = "create_time")
    val createTime: String,
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "is_active")
    val isActive: Boolean,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "update_time")
    val updateTime: String,
    @SerialName(value = "url")
    val url: String
)

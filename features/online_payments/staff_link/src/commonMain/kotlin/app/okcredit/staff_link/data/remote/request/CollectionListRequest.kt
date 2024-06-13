package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionListRequest(
    @SerialName(value = "associated_account_ids")
    val associatedAccountIds: List<String>? = null,
    @SerialName(value = "id")
    val id: String,
    @SerialName(value = "name")
    val name: String? = null,
    @SerialName(value = "due_config")
    val dueConfig: DueConfigRequest? = null,
)

@Serializable
data class DueConfigRequest(
    @SerialName(value = "config")
    val config: Int,
    @SerialName(value = "end_time")
    val endTime: Long? = null,
    @SerialName(value = "start_time")
    val startTime: Long? = null,
)

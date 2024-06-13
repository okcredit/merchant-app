package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStaffLinkRequest(
    @SerialName(value = "associated_account_ids")
    val associatedAccountIds: List<String>,
    @SerialName(value = "name")
    val name: String,
    @SerialName(value = "due_config")
    val dueConfig: DueConfigRequest,
    @SerialName(value = "usage_type")
    val usageType: Int,
)

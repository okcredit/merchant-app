package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class UpdateCollectionListRequest(
    @SerialName(value = "action")
    val action: String,
    @SerialName(value = "collection_list")
    val collectionList: CollectionListRequest,
    @SerialName(value = "invoices")
    val invoices: List<String>? = null,
    @SerialName(value = "bills")
    val bills: List<BillItem>? = null,
)

@Serializable
data class BillItem(
    @SerialName(value = "bill_id")
    val billId: String,
    @SerialName(value = "customer_id")
    val customerId: String,
)

enum class EditAction(val value: String) {
    ADD("edit_associations"),
    DELETE("remove_associations"),
    EDIT_NAME("edit_name"),
    MARK_INACTIVE("mark_inactive"),
    DUE_CONFIG("due_config"),
    IGNORE_INVOICES("ignore_invoices"),
    REMOVE_BILLS("remove_bills"),
}

package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PendingTransactionRequest(
    @SerialName(value = "approved_by_merchant")
    val approvedByMerchant: Boolean,
    @SerialName(value = "business_id")
    val businessId: String,
    @SerialName(value = "id")
    val id: String // will get from UI, idList = list<String>
)

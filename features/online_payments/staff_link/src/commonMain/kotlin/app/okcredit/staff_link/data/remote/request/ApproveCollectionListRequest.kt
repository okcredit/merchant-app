package app.okcredit.staff_link.data.remote.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApproveCollectionListRequest(
    @SerialName(value = "transactions")
    val transactions: List<PendingTransactionRequest>
)

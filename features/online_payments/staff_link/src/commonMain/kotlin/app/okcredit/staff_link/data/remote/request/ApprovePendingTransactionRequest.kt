package app.okcredit.staff_link.data.remote.request

import app.okcredit.staff_link.data.remote.request.PendingTransactionRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApprovePendingTransactionRequest(
    @SerialName(value = "transaction")
    val transaction: PendingTransactionRequest
)

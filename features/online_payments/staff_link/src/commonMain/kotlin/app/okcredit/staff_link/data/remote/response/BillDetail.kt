package app.okcredit.staff_link.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BillDetail(
    @SerialName(value = "bill_date")
    val billDate: Long,
    @SerialName(value = "bill_id")
    val billId: String,
    @SerialName(value = "bill_info")
    val billInfo: String?,
    @SerialName(value = "bill_number")
    val billNumber: String,
    @SerialName(value = "created_at")
    val createdAt: Long,
    @SerialName(value = "customer_id")
    val customerId: String,
    @SerialName(value = "deleted_at")
    val deletedAt: Long?,
    @SerialName(value = "due_date")
    val dueDate: Long?,
    @SerialName(value = "latest_settlement_date")
    val latestSettlementDate: Long,
    @SerialName(value = "status")
    val status: Int,
    @SerialName(value = "total_amount")
    val totalAmount: Long,
    @SerialName(value = "total_paid_amount")
    val totalPaidAmount: Long,
    @SerialName(value = "transaction_id")
    val transactionId: String?,
    @SerialName(value = "updated_at")
    val updatedAt: Long
)

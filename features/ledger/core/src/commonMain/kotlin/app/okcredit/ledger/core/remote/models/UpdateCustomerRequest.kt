package app.okcredit.ledger.core.remote.models

import app.okcredit.ledger.contract.model.Customer
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateCustomerRequest(
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("description")
    val description: String,
    @SerialName("address")
    val address: String?,
    @SerialName("gst_number")
    val gstNumber: String?,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("lang")
    val lang: String?,
    @SerialName("reminder_mode")
    val reminderMode: String?,
    @SerialName("txn_alert_enabled")
    val txnAlertEnabled: Boolean,
    @SerialName("update_txn_alert_enabled")
    val updateTxnAlertEnabled: Boolean,
    @SerialName("display_txn_alert_setting")
    val displayTxnAlertSetting: Boolean,
    @SerialName("update_display_txn_alert_setting")
    val updateDisplayTxnAlertSetting: Boolean,
    @SerialName("due_custom_date")
    val dueCustomDate: Long?,
    @SerialName("update_due_custom_date")
    val updateDueCustomDate: Boolean,
    @SerialName("delete_due_custom_date")
    val deleteDueCustomDate: Boolean,
    @SerialName("update_add_transaction_restricted")
    val updateAddTransactionRestricted: Boolean,
    @SerialName("add_transaction_restricted")
    val addTransactionRestricted: Boolean,
    @SerialName("state")
    val state: Int,
    @SerialName("update_state")
    val updateState: Boolean,
    @SerialName("updated_at")
    val updatedAt: Long,
)

fun Customer.createUpdateCustomerRequest(): UpdateCustomerRequest {
    return UpdateCustomerRequest(
        mobile = this.mobile,
        description = this.name,
        address = "",
        gstNumber = this.gstNumber,
        profileImage = this.profileImage,
        lang = this.settings.language,
        reminderMode = this.settings.reminderMode,
        txnAlertEnabled = this.settings.txnAlertEnabled,
        updateTxnAlertEnabled = this.settings.txnAlertEnabled,
        displayTxnAlertSetting = this.settings.txnAlertEnabled,
        updateDisplayTxnAlertSetting = false,
        dueCustomDate = this.dueDate?.epochMillis ?: 0L,
        updateDueCustomDate = false,
        deleteDueCustomDate = false,
        updateAddTransactionRestricted = false,
        addTransactionRestricted = this.settings.addTransactionRestricted,
        state = if (this.settings.blockedByCustomer) 3 else 1,
        updateState = false,
        updatedAt = Clock.System.now().toEpochMilliseconds(),
    )
}

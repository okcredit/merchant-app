package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ApiCustomer(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String?,
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("status")
    val status: Int,
    @SerialName("description")
    val description: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("txn_start_time")
    val txnStartTime: Long?,
    @SerialName("updated_at")
    val updatedAt: Long,
    @SerialName("account_url")
    val accountUrl: String?,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("gst_number")
    val gstNumber: String?,
    @SerialName("email")
    val email: String?,
    @SerialName("registered")
    val registered: Boolean,
    @SerialName("txn_alert_enabled")
    val txnAlertEnabled: Boolean,
    @SerialName("lang")
    val lang: String?,
    @SerialName("reminder_mode")
    val reminderMode: String?,
    @SerialName("due_custom_date")
    val dueCustomDate: String?,
    @SerialName("due_reminder_enabled_set")
    val dueReminderEnabledSet: Boolean?,
    @SerialName("due_credit_period_set")
    val dueCreditPeriodSet: Boolean?,
    @SerialName("is_live_sales")
    val isLiveSales: Boolean,
    @SerialName("display_txn_alert_setting")
    val displayTxnAlertSetting: Boolean?,
    @SerialName("add_transaction_restricted")
    val addTransactionRestricted: Boolean,
    @SerialName("state")
    val state: Int,
    @SerialName("blocked_by_customer")
    val blockedByCustomer: Boolean,
    @SerialName("restrict_contact_sync")
    val restrictContactSync: Boolean,
    @SerialName("last_reminder_sent")
    val lastReminderSent: String?,
)

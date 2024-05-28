package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName

data class ApiSupplier(
    @SerialName("id")
    val id: String,
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("status")
    val status: Int,
    @SerialName("name")
    val name: String,
    @SerialName("create_time")
    val createTime: Long,
    @SerialName("txn_start_time")
    val txnStartTime: Long?,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("registered")
    val registered: Boolean,
    @SerialName("txn_alert_enabled")
    val txnAlertEnabled: Boolean,
    @SerialName("lang")
    val lang: String?,
    @SerialName("display_txn_alert_setting")
    val displayTxnAlertSetting: Boolean?,
    @SerialName("add_transaction_restricted")
    val addTransactionRestricted: Boolean,
    @SerialName("state")
    val state: Int,
    @SerialName("blocked_by_supplier")
    val blockedBySupplier: Boolean,
    @SerialName("restrict_contact_sync")
    val restrictContactSync: Boolean,
)

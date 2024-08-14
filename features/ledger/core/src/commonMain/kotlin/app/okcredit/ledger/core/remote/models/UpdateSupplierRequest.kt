package app.okcredit.ledger.core.remote.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateSupplierRequest(
    @SerialName("supplier")
    val supplier: SupplierRequestForUpdate,
    @SerialName("update_txn_alert_enabled")
    val updateTxnAlertEnabled: Boolean,
    @SerialName("update_display_txn_alert_setting")
    val updateDisplayTxnAlertSetting: Boolean,
    @SerialName("state")
    val state: Int,
    @SerialName("update_state")
    val updateState: Boolean,
    @SerialName("update_time")
    val updateTime: Long,
)

@Serializable
data class SupplierRequestForUpdate(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("mobile")
    val mobile: String?,
    @SerialName("address")
    val address: String?,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("lang")
    val lang: String?,
    @SerialName("txn_alert_enabled")
    val txnAlertEnabled: Boolean,
    @SerialName("state")
    val state: Int,
    @SerialName("display_txn_alert_setting")
    val displayTxnAlertSetting: Boolean,
)

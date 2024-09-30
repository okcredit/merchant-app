package app.okcredit.ledger.core.remote.models

import app.okcredit.ledger.contract.model.Supplier
import kotlinx.datetime.Clock
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

fun Supplier.createUpdateSupplierRequest(
    id: String,
    name: String,
    mobile: String?,
    profileImage: String?,
    lang: String?,
    txnAlertEnabled: Boolean,
    state: Int,
    address: String?,
): UpdateSupplierRequest {
    return UpdateSupplierRequest(
        supplier = SupplierRequestForUpdate(
            id = id,
            name = name,
            mobile = mobile,
            profileImage = profileImage,
            lang = lang,
            txnAlertEnabled = txnAlertEnabled,
            state = state,
            displayTxnAlertSetting = false,
            address = address,
        ),
        updateTxnAlertEnabled = this.settings.txnAlertEnabled,
        updateDisplayTxnAlertSetting = false,
        state = if (this.settings.blockedBySupplier) 3 else 1,
        updateState = true,
        updateTime = Clock.System.now().toEpochMilliseconds(),
    )
}

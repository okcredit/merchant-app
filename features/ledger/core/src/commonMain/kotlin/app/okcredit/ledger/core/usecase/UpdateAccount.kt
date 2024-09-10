package app.okcredit.ledger.core.usecase

sealed class RequestUpdateAccount {
    data class UpdateAddress(
        val address: String,
    ) : RequestUpdateAccount()

    data class UpdateMobile(
        val mobile: String,
    ) : RequestUpdateAccount()

    data class UpdateGstNumber(
        val gstNumber: String,
    ) : RequestUpdateAccount()

    data class UpdateAccountState(
        val blocked: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateReminderMode(
        val reminderMode: String,
    ) : RequestUpdateAccount()

    data class UpdateName(
        val desc: String,
    ) : RequestUpdateAccount()

    data class UpdateProfileImage(
        val profileImage: String,
    ) : RequestUpdateAccount()

    data class UpdateLanguage(
        val lang: String,
    ) : RequestUpdateAccount()

    data class UpdateTransactionAlertStatus(
        val isEnable: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateAddTransactionRestrictedStatus(
        val isAddTransactionRestricted: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateTransactionAlertStatusAndLanguage(
        val isEnable: Boolean,
        val lang: String?,
    ) : RequestUpdateAccount()

    data object DeleteDueCustomDate : RequestUpdateAccount()

    data class UpdateDisplayedTxnAlertStatus(
        val isEnable: Boolean,
    ) : RequestUpdateAccount()

    data class UpdateDueCustomDate(val dueCustomDate: Long) : RequestUpdateAccount()
}


class UpdateAccount {
}
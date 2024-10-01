package app.okcredit.ledger.ui.statement

import app.okcredit.ledger.ui.model.TransactionData
import app.okcredit.ledger.ui.model.TransactionDueInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Timestamp
import okcredit.base.units.timestamp

interface AccountStatementContract {

    data class State(
        val isLoading: Boolean = true,
        val isLoadingDownload: Boolean = false,
        val transactions: List<TransactionDueInfo> = arrayListOf(),
        val totalPaymentAmount: Long = 0,
        val totalPaymentCount: Int = 0,
        val totalCreditCount: Int = 0,
        var totalCreditAmount: Long = 0,
        val isAlertVisible: Boolean = false,
        val alertMessage: String = "",
        val isShowOld: Boolean = false,
        val error: Boolean = false,
        val networkError: Boolean = false,
        val isShowDownloadAlert: Boolean = false,
        val startDate: Timestamp = Clock.System.now().timestamp,
        val endDate: Timestamp = getEndDate(),
        val isOnlineTransactionSelected: Boolean = false,
        val sourceScreen: String = "",
        val totalDiscountAmount: Long = 0,
        val totalDiscountCount: Int = 0,
        val downloadedFileUriString: String? = null
    ) : UiState {

        companion object {
            fun getEndDate(): Timestamp {
                val currentTime = Clock.System.now()
                val timeZone = TimeZone.currentSystemDefault()
                return currentTime.plus(1, DateTimeUnit.DAY, timeZone).timestamp
            }
        }
    }


    sealed class PartialState : UiState.Partial {

        data object ShowLoading : PartialState()

        data class ShowData(
            val transaction: List<TransactionDueInfo>,
            var totalCreditAmount: Long,
            val totalCreditCount: Int,
            val totalPaymentAmount: Long,
            val totalPaymentCount: Int,
            val isShowOld: Boolean,
            val totalDiscountAmount: Long,
            val totalDiscountCount: Int
        ) : PartialState()

        data object ErrorState : PartialState()

        data class ShowAlert(val message: String) : PartialState()

        data class ChangeDateRange(val startDate: Timestamp, val endDate: Timestamp) :
            PartialState()

        data object HideAlert : PartialState()

        data class SetNetworkError(val networkError: Boolean) : PartialState()

        data class ShowDownloadAlert(val downloadedFilePath: String?) : PartialState()

        data object HideDownloadAlert : PartialState()

        data class ChangeShowOldStatus(val status: Boolean) : PartialState()

        data class SetDownloadLoadingStatus(val status: Boolean) : PartialState()

        data class SetOnlineTxnSelected(val isOnlineTransactionSelected: Boolean) : PartialState()

        data class SetSourceScreen(
            val sourceScreen: String,
            val isOnlineTransactionSelected: Boolean
        ) : PartialState()
    }

    sealed class Intent : UserIntent {
        data class ShowAlert(val message: String) : Intent()

        data object LoadOldTxns : Intent()

        data class ChangeDateRange(val startDate: Timestamp, val endDate: Timestamp) : Intent()

        data class DownloadStatement(val startDate: Timestamp, val endDate: Timestamp) : Intent()

        data object HideDownloadAlert : Intent()

        data class SelectOnlineTransactions(val isOnlineTranslationSelected: Boolean) : Intent()

        // data class ObserveWorkerStatus(val weakLifecycleOwner: WeakReference<LifecycleOwner>) : Intent()
    }

    sealed class ViewEvent : BaseViewEvent
}
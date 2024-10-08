package app.okcredit.ledger.ui.customer

import app.okcredit.ledger.ui.customer.usecase.TrackWithIntent.EventType
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.ToolbarData
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Paisa

interface CustomerLedgerContract {
    data class State(
        val loading: Boolean = false,
        val ledgerItems: List<LedgerItem> = emptyList(),
        val customerDetails: CustomerDetails? = null,
        val toolbarData: ToolbarData? = null,
        val transactionScrollPosition: Int? = null,
        val errorMessage: String? = null,
    ) : UiState

    data class CustomerDetails(
        val id: String,
        val name: String,
        val mobile: String?,
        val address: String,
        val profileImage: String?,
        val balance: Paisa,
        val formattedDueDate: String,
        val blockerByCustomer: Boolean,
        val blocked: Boolean,
        val reminderMode: String,
        val registered: Boolean,
    )

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()
        data class SetCustomerDetails(val customerDetails: CustomerDetails) : PartialState()
        data class SetToolbarData(val toolbarData: ToolbarData) : PartialState()
        data class SetLedgerItems(val ledgerItems: List<LedgerItem>) : PartialState()
        data class SetLoading(val loading: Boolean) : PartialState()
        data class SetError(val error: String) : PartialState()
    }

    sealed class Intent : UserIntent {
        data class LoadTransactions(val showOldClicked: Boolean, val customerId: String) : Intent()
        data object OnCallClicked : Intent()
        data class TrackWithIntent(val eventType: EventType) : Intent()
        data object OnErrorToastDismissed : Intent()
    }

    sealed class ViewEvent : BaseViewEvent
}

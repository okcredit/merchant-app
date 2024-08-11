package app.okcredit.ledger.ui.supplier

import app.okcredit.ledger.ui.customer.CustomerLedgerContract.CustomerDetails
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.ToolbarData
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Paisa
import org.jetbrains.compose.resources.StringResource


interface SupplierLedgerContract {
    data class State(
        val loading: Boolean = false,
        val ledgerItems: List<LedgerItem> = emptyList(),
        val supplierDetails: SupplierDetails? = null,
        val toolbarData: ToolbarData? = null,
        val transactionScrollPosition: Int? = null
    ) : UiState

    data class SupplierDetails(
        val id: String,
        val name: String,
        val mobile: String?,
        val profileImage: String?,
        val balance: Paisa,
        val formattedDueDate: String,
        val blockedBySupplier: Boolean,
        val blocked: Boolean,
        val reminderMode: String,
        val registered: Boolean,
    )

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()
        data class SetSupplierData(
            val supplierDetails: SupplierDetails,
            val toolbarData: ToolbarData
        ) : PartialState()

        data class SetLoading(val loading: Boolean) : PartialState()
        data class SetLedgerItems(val ledgerItems: List<LedgerItem>) : PartialState()
    }

    sealed class Intent : UserIntent {
        data class LoadTransaction(val showOldClicked: Boolean) : Intent()
    }

    sealed class ViewEvent : BaseViewEvent {
       data class ShowError(val message: StringResource) : ViewEvent()
    }

}
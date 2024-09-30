package app.okcredit.ledger.ui.delete

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.ui.profile.usecase.GetAccountDetails
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import okcredit.base.units.Paisa

interface DeleteAccountContract {

    data class State(
        val isLoading: Boolean = false,
        val accountId: String = "",
        val name: String = "",
        val balance: Paisa = Paisa.ZERO,
        val mobile: String = "",
        val shouldSettle: Boolean = false,
        val accountType: AccountType = AccountType.CUSTOMER,
        val errorMessage: String? = null,
    ) : UiState

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()
        data class SetRelationshipDetails(val res: GetAccountDetails.Response) : PartialState()
        data class SetLoading(val value: Boolean) : PartialState()
        data class SetError(val message: String) : PartialState()
    }

    sealed class Intent : UserIntent {
        data class Load(val accountId: String, val accountType: AccountType) : Intent()
        data class DeleteRelationshipClicked(val accountId: String, val accountType: AccountType) : Intent()
        data object Delete : Intent()
        data object OnSettlementClicked : Intent()
        data object SyncTransactionAndRetryDelete : Intent()
    }

    sealed class ViewEvent : BaseViewEvent {
        data object FinishActivity : ViewEvent()
        data object ShowRetryDialog : ViewEvent()
        data class GoToAddTransactionScreen(val type: Transaction.Type, val balance: Paisa) :
            ViewEvent()
    }
}

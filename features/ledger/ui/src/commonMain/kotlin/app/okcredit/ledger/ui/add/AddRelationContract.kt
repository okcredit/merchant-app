package app.okcredit.ledger.ui.add

import app.okcredit.ledger.contract.model.AccountType
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface AddRelationContract {

    data class State(
        val loading: Boolean = false,
        val errorDialog: ErrorDialog? = null,
    ) : UiState

    sealed interface ErrorDialog {
        data class MobileConflictError(val accountId: String, val name: String) : ErrorDialog

        data class CyclicAccountError(val accountId: String, val name: String) : ErrorDialog
    }

    sealed interface PartialState : UiState.Partial {
        data class SetLoading(val loading: Boolean) : PartialState

        data class SetErrorDialog(val errorDialog: ErrorDialog?) : PartialState
    }

    sealed interface Intent : UserIntent {
        data object OnDismissDialog : Intent

        data class OnSubmitClicked(
            val name: String,
            val mobile: String?,
            val accountType: AccountType,
        ) : Intent
    }

    sealed interface ViewEvent : BaseViewEvent {
        data class Success(val accountId: String) : ViewEvent
    }
}

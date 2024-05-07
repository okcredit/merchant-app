package app.okcredit.merchant.home

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import tech.okcredit.identity.contract.model.Business

interface HomeContract {

    data class State(
        val isLoading: Boolean = true,
        val business: Business? = null,
    ) : UiState

    sealed class PartialState : UiState.Partial {
        object NoChange : PartialState()

        data class SetBusiness(val business: Business) : PartialState()
    }

    sealed class ViewEvent : BaseViewEvent {
        object GoToLogin : ViewEvent()
        data class ShowToast(val message: String) : ViewEvent()
    }

    sealed class Intent : UserIntent {
        object OnResume : Intent()
    }
}

const val SCREEN_NAME = "home_screen"

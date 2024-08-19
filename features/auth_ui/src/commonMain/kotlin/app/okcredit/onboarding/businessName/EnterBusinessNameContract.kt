package app.okcredit.onboarding.businessName

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface EnterBusinessNameContract {

    data class State(
        val loading: Boolean = false,
    ) : UiState

    sealed class PartialState : UiState.Partial {
        data object ShowLoading : PartialState()
        data object HideLoading : PartialState()
    }

    sealed class Intent : UserIntent {
        data class SetBusinessName(val businessName: String) : Intent()
    }

    sealed class ViewEvent : BaseViewEvent {
        object GoToHome : ViewEvent()
    }
}

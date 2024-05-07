package app.okcredit.merchant.splash

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface SplashContract {

    data class State(
        val loading: Boolean = false,
    ) : UiState

    sealed class PartialState : UiState.Partial

    sealed class Intent : UserIntent

    sealed class ViewEvent : BaseViewEvent {
        object MoveToHome : ViewEvent()
        object MoveToLogin : ViewEvent()
    }
}

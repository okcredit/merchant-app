package app.okcredit.merchant.sync

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface SyncContract {

    data class State(
        val loading: Boolean = false,
    ) : UiState

    sealed class PartialState : UiState.Partial

    sealed class Intent : UserIntent

    sealed class ViewEvent : BaseViewEvent {
        data object GoToHome : ViewEvent()

        data object GoToSelectBusiness : ViewEvent()
    }
}

package app.okcredit.web.ui

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface WebContract {

    data class State(
        val cookie: String = "",
        val activeBusinessId: String = "",
    ) : UiState

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()

        data class SetCookie(val cookie: String) : PartialState()

        data class SetActiveBusinessId(val activeBusinessId: String) : PartialState()
    }

    sealed interface Intent : UserIntent {
        data class OnWhatsAppRequest(val mobile: String, val message: String, val imageUrl: String?) : Intent
    }

    sealed interface ViewEvent : BaseViewEvent
}

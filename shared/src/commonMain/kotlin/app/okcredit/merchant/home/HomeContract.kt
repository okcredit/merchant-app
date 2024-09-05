package app.okcredit.merchant.home

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent

interface HomeContract {

    data class State(
        val loading: Boolean = false,
        val moreOptions: List<MoreOptionItem> = emptyList(),
    ) : UiState

    sealed interface PartialState : UiState.Partial {
        data object NoChange : PartialState
        data class SetHomeMoreOptionItems(val items: List<MoreOptionItem>) : PartialState
    }

    sealed interface Intent : UserIntent

    sealed interface ViewEvent : BaseViewEvent
}

package app.okcredit.merchant.selectBusiness

import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.UiState
import okcredit.base.ui.UserIntent
import tech.okcredit.identity.contract.model.Business

interface SelectBusinessContract {

    data class State(
        val loading: Boolean = false,
        val businesses: List<Business> = emptyList(),
    ) : UiState

    sealed class PartialState : UiState.Partial {
        data object NoChange : PartialState()
        data class SetBusinesses(val businesses: List<Business>) : PartialState()
    }

    sealed class Intent : UserIntent {
        data class OnBusinessSelected(val businessId: String) : Intent()
    }

    sealed class ViewEvent : BaseViewEvent {
        data object MoveToHome : ViewEvent()
    }
}
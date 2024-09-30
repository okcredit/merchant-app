package app.okcredit.web.ui

import app.okcredit.web.ui.WebContract.Intent
import app.okcredit.web.ui.WebContract.PartialState
import app.okcredit.web.ui.WebContract.State
import app.okcredit.web.ui.WebContract.ViewEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import me.tatarka.inject.annotations.Inject
import okcredit.base.PlatformExtensions
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class WebScreenScreenModel(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val platformExtension: PlatformExtensions,
) : BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {
    override fun partialStates(): Flow<PartialState> {
        return merge(
            loadActiveBusinessId(),
            observeOnWhatsAppRequest(),
        )
    }

    private fun loadActiveBusinessId() = wrap {
        getActiveBusinessId.execute()
    }.map {
        when (it) {
            is Result.Failure -> PartialState.NoChange
            is Result.Progress -> PartialState.NoChange
            is Result.Success -> PartialState.SetActiveBusinessId(it.value)
        }
    }

    private fun observeOnWhatsAppRequest() = intent<Intent.OnWhatsAppRequest>()
        .map {
            platformExtension.shareOnWhatsApp(mobileNumber = it.mobile, text = it.message)
            PartialState.NoChange
        }

    override fun reduce(
        currentState: State,
        partialState: PartialState
    ): State {
        return when (partialState) {
            PartialState.NoChange -> currentState
            is PartialState.SetActiveBusinessId -> currentState.copy(
                activeBusinessId = partialState.activeBusinessId
            )
            is PartialState.SetCookie -> currentState.copy(
                cookie = partialState.cookie
            )
        }
    }
}
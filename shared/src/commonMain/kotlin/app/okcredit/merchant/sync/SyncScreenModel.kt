package app.okcredit.merchant.sync

import app.okcredit.merchant.sync.SyncContract.*
import app.okcredit.merchant.usecase.LoginDataSyncer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result

@Inject
class SyncScreenModel(private val loginDataSyncer: LoginDataSyncer) :
    BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(loadAuthState())
    }

    private fun loadAuthState() = wrap {
        loginDataSyncer.execute()
    }.onEach {
        if (it is Result.Success) {
            emitViewEvent(ViewEvent.GoToHome)
        }
    }.dropAll()

    override fun reduce(currentState: State, partialState: PartialState): State {
        return currentState
    }
}

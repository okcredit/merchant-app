package app.okcredit.merchant.splash

import app.okcredit.merchant.splash.SplashContract.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseCoroutineScreenModel
import okcredit.base.ui.Result
import tech.okcredit.auth.AuthService

@Inject
class SplashScreenModel(private val authService: AuthService) :
    BaseCoroutineScreenModel<State, PartialState, ViewEvent, Intent>(State()) {

    override fun partialStates(): Flow<PartialState> {
        return merge(loadAuthState())
    }

    private fun loadAuthState() = wrap {
        delay(1_000)
        authService.isAuthenticated()
    }
        .onEach {
            if (it is Result.Success) {
                if (it.value) {
                    emitViewEvent(ViewEvent.MoveToHome)
                } else {
                    emitViewEvent(ViewEvent.MoveToLogin)
                }
            }
        }.dropAll()

    override fun reduce(currentState: State, partialState: PartialState): State {
        return currentState
    }
}

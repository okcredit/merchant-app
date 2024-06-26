package app.okcredit.merchant.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.merchant.home.HomeContract.State
import app.okcredit.merchant.home.HomeContract.ViewEvent
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

@Inject
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>(HomeScreenModel::class)

        // collect state and render
        val state by screenModel.states.collectAsState()
        Render(screenModel, state)

        // collect view events and handle
        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }
    }

    @Composable
    private fun Render(screenModel: HomeScreenModel, state: State) {
        HomeUi(
            state = state,
        )
    }

    private fun handleViewEvent(viewEvent: ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            ViewEvent.GoToLogin -> {
            }
            is ViewEvent.ShowToast -> {
            }
        }
    }
}

package app.okcredit.onboarding.businessName

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.shared.contract.SharedScreenRegistry
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

@Inject
class EnterBusinessNameScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<EnterBusinessNameScreenModel>(EnterBusinessNameScreenModel::class)
        val state by screenModel.states.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        EnterBusinessNameUi(
            loading = state.loading,
            onSkipClicked = {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.Home))
            },
            onSubmitClick = {
                screenModel.pushIntent(EnterBusinessNameContract.Intent.SetBusinessName(it))
            },
        )
    }

    private fun handleViewEvent(event: EnterBusinessNameContract.ViewEvent, navigator: Navigator) {
        when (event) {
            EnterBusinessNameContract.ViewEvent.GoToHome -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.Home))
            }
        }
    }
}

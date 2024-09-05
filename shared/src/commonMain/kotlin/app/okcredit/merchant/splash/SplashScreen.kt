package app.okcredit.merchant.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import app.okcredit.onboarding.OnboardingScreenRegistry
import app.okcredit.shared.contract.SharedScreenRegistry
import app.okcredit.ui.ic_okcredit_logo
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel
import org.jetbrains.compose.resources.painterResource

class SplashScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SplashScreenModel>()

        Render()

        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }
    }

    private fun handleViewEvent(viewEvent: SplashContract.ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            SplashContract.ViewEvent.MoveToHome -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.Home))
            }

            SplashContract.ViewEvent.MoveToLogin -> {
                navigator.replaceAll(ScreenRegistry.get(OnboardingScreenRegistry.Login))
            }
        }
    }

    @Composable
    private fun Render() {
        Box(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.onPrimary)) {
            Image(
                painter = painterResource(app.okcredit.ui.Res.drawable.ic_okcredit_logo),
                modifier = Modifier.align(Alignment.Center),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            )
        }
    }
}

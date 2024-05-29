package app.okcredit.onboarding.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.onboarding.OnboardingScreenRegistry
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
class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<LoginScreenModel>(LoginScreenModel::class)
        val state by screenModel.states.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        Render(screenModel, state)
    }

    @Composable
    fun Render(screenModel: LoginScreenModel, state: LoginContract.State) {
        if (state.showEnterOtp) {
            EnterOtpUi(
                state = state,
                otpEntered = { screenModel.pushIntent(LoginContract.Intent.OtpEntered(it)) },
                resendOtpOnSms = { screenModel.pushIntent(LoginContract.Intent.ResendOtpOnSms) },
                resendOtpOnWhatsApp = { screenModel.pushIntent(LoginContract.Intent.ResendOtpOnWhatsApp) },
                resendOtpOnRecoveryNumber = { screenModel.pushIntent(LoginContract.Intent.ResendOtpOnRecoveryNumber) },
            )
        } else {
            EnterMobile { screenModel.pushIntent(LoginContract.Intent.ValidMobileEntered(it)) }
        }
    }

    private fun handleViewEvent(event: LoginContract.ViewEvent, navigator: Navigator) {
        when (event) {
            LoginContract.ViewEvent.GoToEnterBusinessName -> {
                navigator.replaceAll(ScreenRegistry.get(OnboardingScreenRegistry.EnterBusinessName))
            }
            LoginContract.ViewEvent.GoToSyncScreen -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.SyncData))
            }
        }
    }
}

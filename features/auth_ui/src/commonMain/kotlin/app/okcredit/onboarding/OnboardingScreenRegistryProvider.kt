package app.okcredit.onboarding

import app.okcredit.onboarding.businessName.EnterBusinessNameScreen
import app.okcredit.onboarding.login.LoginScreen
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import me.tatarka.inject.annotations.Inject

@Inject
class OnboardingScreenRegistryProvider(
    private val loginScreen: () -> LoginScreen,
    private val enterBusinessNameScreen: () -> EnterBusinessNameScreen,
) {

    fun screenRegistry() = screenModule {
        register<OnboardingScreenRegistry.Login> {
            loginScreen.invoke()
        }

        register<OnboardingScreenRegistry.EnterBusinessName> {
            enterBusinessNameScreen.invoke()
        }
    }
}

sealed class OnboardingScreenRegistry : ScreenProvider {
    object Login : OnboardingScreenRegistry()

    object EnterBusinessName : OnboardingScreenRegistry()
}

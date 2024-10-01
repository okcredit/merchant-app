package app.okcredit.onboarding

import app.okcredit.onboarding.businessName.EnterBusinessNameScreen
import app.okcredit.onboarding.login.LoginScreen
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule

object OnboardingScreenRegistryProvider {

    fun screenRegistry() = screenModule {
        register<OnboardingScreenRegistry.Login> {
            LoginScreen()
        }

        register<OnboardingScreenRegistry.EnterBusinessName> {
            EnterBusinessNameScreen()
        }
    }
}

sealed class OnboardingScreenRegistry : ScreenProvider {
    object Login : OnboardingScreenRegistry()

    object EnterBusinessName : OnboardingScreenRegistry()
}

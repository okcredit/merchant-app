package app.okcredit.merchant

import androidx.compose.ui.window.ComposeUIViewController
import app.okcredit.ledger.ui.LedgerScreenRegistryProvider
import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.web.WebScreenRegistryProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import platform.UIKit.UIViewController

fun mainViewController(): UIViewController {
    val applicationComponent = ApplicationComponent.createKmp(
        baseUrl = "https://okapis.io/",
        appVersion = "1.0.0",
        versionCode = 100,
        debug = false,
        flavor = "prod",
    )
    initScreenRegistry()
    applicationComponent.appInitializers.forEach { it.init() }
    return ComposeUIViewController { MerchantApplication(applicationComponent.appScreenModelFactory) }
}

private fun initScreenRegistry() {
    ScreenRegistry {
        SharedScreenRegistryProvider.screenRegistry().invoke(this)
        OnboardingScreenRegistryProvider.screenRegistry().invoke(this)
        LedgerScreenRegistryProvider.screenRegistry().invoke(this)
        WebScreenRegistryProvider.screenRegistry().invoke(this)
    }
}

package app.okcredit.merchant

import androidx.compose.ui.window.ComposeUIViewController
import cafe.adriel.voyager.core.registry.ScreenRegistry
import platform.UIKit.UIViewController

fun mainViewController(): UIViewController {
    val applicationComponent = ApplicationComponent::class.create(
        baseUrl = "https://okapis.io/",
        appVersion = "1.0.0",
        versionCode = 100,
        debug = false,
        flavor = "prod",
    )
    initScreenRegistry(applicationComponent)
    return ComposeUIViewController { MerchantApplication(applicationComponent.appScreenModelFactory) }
}

private fun initScreenRegistry(component: ApplicationComponent) {
    ScreenRegistry {
        component.onboardingScreenRegistryProvider.screenRegistry().invoke(this)
        component.sharedScreenRegistryProvider.screenRegistry().invoke(this)
    }
}

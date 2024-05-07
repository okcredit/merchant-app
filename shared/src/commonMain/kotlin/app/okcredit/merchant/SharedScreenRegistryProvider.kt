package app.okcredit.merchant

import app.okcredit.merchant.home.HomeScreen
import app.okcredit.merchant.splash.SplashScreen
import app.okcredit.merchant.sync.SyncScreen
import app.okcredit.shared.contract.SharedScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import me.tatarka.inject.annotations.Inject

@Inject
class SharedScreenRegistryProvider(
    private val homeScreen: () -> HomeScreen,
    private val splashScreen: () -> SplashScreen,
    private val syncScreen: () -> SyncScreen,
) {

    fun screenRegistry() = screenModule {
        register<SharedScreenRegistry.Home> {
            homeScreen.invoke()
        }

        register<SharedScreenRegistry.Splash> {
            splashScreen.invoke()
        }

        register<SharedScreenRegistry.SyncData> {
            syncScreen.invoke()
        }

    }
}

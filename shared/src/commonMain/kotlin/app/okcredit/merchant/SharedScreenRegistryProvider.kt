package app.okcredit.merchant

import app.okcredit.merchant.home.HomeScreen
import app.okcredit.merchant.selectBusiness.SelectBusinessScreen
import app.okcredit.merchant.splash.SplashScreen
import app.okcredit.merchant.sync.SyncScreen
import app.okcredit.shared.contract.SharedScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import me.tatarka.inject.annotations.Inject

@Inject
class SharedScreenRegistryProvider {

    fun screenRegistry() = screenModule {
        register<SharedScreenRegistry.Home> {
            HomeScreen()
        }

        register<SharedScreenRegistry.Splash> {
            SplashScreen()
        }

        register<SharedScreenRegistry.SyncData> {
            SyncScreen()
        }

        register<SharedScreenRegistry.SelectBusiness> {
            SelectBusinessScreen()
        }
    }
}

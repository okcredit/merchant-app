package app.okcredit.merchant

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import app.okcredit.shared.contract.SharedScreenRegistry
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.OkPayTheme
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.LocalScreenModelFactory

@Composable
fun MerchantApplication(appScreenModelFactory: AppScreenModelFactory) {
    OkCreditTheme {
        CompositionLocalProvider(LocalScreenModelFactory provides appScreenModelFactory) {
            Navigator(
                screen = ScreenRegistry.get(SharedScreenRegistry.Splash),
                onBackPressed = {
                    Logger.d { "Back pressed from $it" }
                    true
                },
            )
        }
    }
}

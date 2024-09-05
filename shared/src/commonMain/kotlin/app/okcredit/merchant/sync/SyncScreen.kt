package app.okcredit.merchant.sync

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.shared.contract.SharedScreenRegistry
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

class SyncScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SyncScreenModel>()

        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents { handleViewEvent(it, navigator) }

        Render()
    }

    private fun handleViewEvent(viewEvent: SyncContract.ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            SyncContract.ViewEvent.GoToHome -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.Home))
            }

            SyncContract.ViewEvent.GoToSelectBusiness -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.SelectBusiness))
            }
        }
    }

    @Composable
    private fun Render() {
        Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            Spacer(Modifier.weight(1.0f))
            Text(
                text = "Downloading your data",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(Modifier.height(32.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.weight(1.0f))
        }
    }
}

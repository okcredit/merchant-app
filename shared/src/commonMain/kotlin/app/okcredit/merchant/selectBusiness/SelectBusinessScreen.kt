@file:OptIn(ExperimentalMaterial3Api::class)

package app.okcredit.merchant.selectBusiness

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import tech.okcredit.identity.contract.model.Business

class SelectBusinessScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SelectBusinessScreenModel>()

        val state by screenModel.states.collectAsState()
        SelectBusinessScreenUi(
            state = state,
            onBusinessSelected = {
                screenModel.pushIntent(SelectBusinessContract.Intent.OnBusinessSelected(it))
            }
        )

        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents { handleViewEvent(it, navigator) }

    }

    private fun handleViewEvent(viewEvent: SelectBusinessContract.ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            SelectBusinessContract.ViewEvent.MoveToHome -> {
                navigator.replaceAll(ScreenRegistry.get(SharedScreenRegistry.Home))
            }
        }
    }
}

@Composable
fun SelectBusinessScreenUi(
    state: SelectBusinessContract.State,
    onBusinessSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Business", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) {
        BusinessList(
            modifier = Modifier.padding(it),
            businesses = state.businesses,
            onBusinessSelected = onBusinessSelected
        )
    }
}

@Composable
fun BusinessList(
    businesses: List<Business>,
    onBusinessSelected: (String) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        items(businesses) { business ->
            BusinessItem(business, onBusinessSelected)
        }
    }
}

@Composable
fun BusinessItem(business: Business, onBusinessSelected: (String) -> Unit) {
    Surface(
        onClick = { onBusinessSelected(business.id) },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row {
            Text(
                text = business.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(Modifier.weight(1.0f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )
        }

    }
}

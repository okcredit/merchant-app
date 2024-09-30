package app.okcredit.merchant.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.okcredit.ledger.ui.LedgerScreenRegistry
import app.okcredit.merchant.search.composable.HomeSearchUi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.moveTo
import okcredit.base.di.rememberScreenModel

data class HomeSearchScreen(val currentTab: String) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeSearchScreenModel>()
        val state = screenModel.states.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        HomeSearchUi(
            state = state.value,
            onAddToAccountClicked = {
                screenModel.pushIntent(HomeSearchContract.Intent.AddAccountFromContact(it))
            },
            onSearchQueryChange = {
                screenModel.pushIntent(HomeSearchContract.Intent.SearchQueryChanged(it))
            },
            onBackPress = {
                navigator.pop()
            },
            onContactItemClicked = {},
            onCustomerItemClicked = { customerId ->
                navigator.moveTo(
                    LedgerScreenRegistry.CustomerLedger(customerId),
                )
            },
            onCustomerProfileClicked = {},
            onCustomerWhatsAppClicked = {},
            onSupplierItemClicked = { supplierId ->
                navigator.moveTo(
                    LedgerScreenRegistry.SupplierLedger(supplierId),
                )
            },
            onSupplierProfileClicked = {},
        )
    }
}

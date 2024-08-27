package app.okcredit.merchant.ledger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.ui.LedgerScreenRegistry
import app.okcredit.ledger.ui.LedgerScreenRegistryProvider
import app.okcredit.ledger.ui.customer.CustomerLedgerScreen
import app.okcredit.ledger.ui.supplier.SupplierLedgerScreen
import app.okcredit.merchant.ledger.HomeContract.State
import app.okcredit.merchant.ledger.HomeContract.ToolbarAction.*
import app.okcredit.merchant.ledger.HomeContract.ViewEvent
import app.okcredit.merchant.ledger.composables.HomeScreenUi
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

object HomeLedgerTab : Tab {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeLedgerScreenModel>()

        // collect state and render
        val state by screenModel.states.collectAsState()
        Render(screenModel, state)

        // collect view events and handle
        val navigator = LocalNavigator.currentOrThrow
        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }
    }

    @Composable
    private fun Render(screenModel: HomeLedgerScreenModel, state: State) {
        HomeScreenUi(
            state = state,
            onTabChanged = {
                val tab = if (it) HomeTab.CUSTOMER_TAB else HomeTab.SUPPLIER_TAB
                screenModel.pushIntent(HomeContract.Intent.OnTabChanged(tab))
            },
            onAvatarClicked = ::onBusinessClicked,
            onToolbarActionClicked = { toolbarAction ->
                when (toolbarAction) {
                    ACTIVATE_UPI -> TODO()
                    NEED_HELP -> TODO()
                    SHARE -> TODO()
                }
            },
            onPrimaryVpaClicked = {},
            onSearchClicked = {},
            onSortAndFilterClicked = {},
            onCustomerClicked = {
                screenModel.pushIntent(HomeContract.Intent.OnCustomerClicked(it))
            },
            onSupplierClicked = {
                screenModel.pushIntent(HomeContract.Intent.OnSupplierClicked(it))
            },
            onCustomerProfileClicked = {},
            onSupplierProfileClicked = {},
            onAddRelationshipClicked = {},
            onDynamicItemClicked = { _, _ -> },
            onSummaryCardClicked = {},
            onPullToRefresh = {},
            onClearFilterClicked = {},
            onUserAlertClicked = {},
        )
    }

    private fun onBusinessClicked() {

    }

    private fun handleViewEvent(viewEvent: ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            ViewEvent.LaunchAskSmsPermissionForAutoReminder -> {}
            is ViewEvent.ShowAutoReminderSummarySnackBar -> {}
            is ViewEvent.ShowError -> {}
            is ViewEvent.GoToCustomerLedgerScreen -> goToCustomerLedgerScreen(
                customerId = viewEvent.customerId,
                navigator = navigator
            )

            is ViewEvent.GoToSupplierLedgerScreen -> goToSupplierLedgerScreen(
                supplierId = viewEvent.supplierId,
                navigator = navigator
            )
        }
    }

    private fun goToSupplierLedgerScreen(supplierId: String, navigator: Navigator) {
        println("Navigating to Supplier Ledger Screen")
        navigator.parent?.push(ScreenRegistry.get(LedgerScreenRegistry.SupplierLedger(supplierId)))
    }

    private fun goToCustomerLedgerScreen(customerId: String, navigator: Navigator) {
        println("Navigating to Customer Ledger Screen")
        navigator.parent?.push(ScreenRegistry.get(LedgerScreenRegistry.CustomerLedger(customerId)))
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            title = "Home",
            icon = null,
            index = 0u,
        )
}

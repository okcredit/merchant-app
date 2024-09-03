package app.okcredit.merchant.ledger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.merchant.ledger.HomeContract.State
import app.okcredit.merchant.ledger.HomeContract.ToolbarAction.*
import app.okcredit.merchant.ledger.HomeContract.ViewEvent
import app.okcredit.merchant.ledger.composables.HomeScreenUi
import app.okcredit.shared.contract.SharedScreenRegistry
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import okcredit.base.di.moveTo
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
        val navigator = LocalNavigator.currentOrThrow.parent
        HomeScreenUi(
            state = state,
            onTabChanged = { selected ->
                val tab = if (selected) HomeTab.SUPPLIER_TAB else HomeTab.CUSTOMER_TAB
                screenModel.pushIntent(HomeContract.Intent.OnTabChanged(tab))
            },
            onAvatarClicked = ::onBusinessClicked,
            onToolbarActionClicked = { toolbarAction ->
                when (toolbarAction) {
                    ACTIVATE_UPI -> {}
                    NEED_HELP -> {}
                    SHARE -> {}
                }
            },
            onPrimaryVpaClicked = {},
            onSearchClicked = {
                navigator?.moveTo(SharedScreenRegistry.Search(currentTab = HomeTab.CUSTOMER_TAB.name))
            },
            onSortAndFilterClicked = {
                screenModel.pushIntent(HomeContract.Intent.OnSortAndFilterClicked)
            },
            onCustomerClicked = {},
            onSupplierClicked = {},
            onCustomerProfileClicked = {},
            onSupplierProfileClicked = {},
            onAddRelationshipClicked = {},
            onDynamicItemClicked = { _, _ -> },
            onSummaryCardClicked = {},
            onPullToRefresh = {},
            onClearFilterClicked = {
                screenModel.pushIntent(HomeContract.Intent.OnSortAndFilterApplied(SortOption.LAST_ACTIVITY, emptySet()))
            },
            onUserAlertClicked = {
                screenModel.pushIntent(HomeContract.Intent.OnRetrySyncTransactionsClicked)
            },
            onDismissDialog = {
                screenModel.pushIntent(HomeContract.Intent.OnDismissDialog)
            },
            onSortAndFilterApplied = { sortOption, reminderFilters ->
                screenModel.pushIntent(HomeContract.Intent.OnSortAndFilterApplied(sortOption, reminderFilters))
            },
        )
    }

    private fun onBusinessClicked() {
    }

    private fun handleViewEvent(viewEvent: ViewEvent, navigator: Navigator) {
        when (viewEvent) {
            is ViewEvent.ShowError -> {}
        }
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            title = "Home",
            icon = null,
            index = 0u,
        )
}

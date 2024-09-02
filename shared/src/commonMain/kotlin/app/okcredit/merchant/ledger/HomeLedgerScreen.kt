package app.okcredit.merchant.ledger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.merchant.ledger.HomeContract.State
import app.okcredit.merchant.ledger.HomeContract.ToolbarAction.*
import app.okcredit.merchant.ledger.HomeContract.ViewEvent
import app.okcredit.merchant.ledger.composables.HomeScreenUi
import app.okcredit.ui.theme.OkCreditTheme
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import me.tatarka.inject.annotations.Inject
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
            onTabChanged = { selected ->
                val tab = if (selected) HomeTab.SUPPLIER_TAB else HomeTab.CUSTOMER_TAB
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
            onSearchClicked = {

            },
            onSortAndFilterClicked = {

            },
            onCustomerClicked = {},
            onSupplierClicked = {},
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

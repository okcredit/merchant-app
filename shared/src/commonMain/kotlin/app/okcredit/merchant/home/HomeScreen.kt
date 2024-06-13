package app.okcredit.merchant.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.merchant.home.HomeContract.State
import app.okcredit.merchant.home.HomeContract.ViewEvent
import app.okcredit.merchant.home.composables.HomeScreenUi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

@Inject
class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>(HomeScreenModel::class)

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
    private fun Render(screenModel: HomeScreenModel, state: State) {
        HomeScreenUi(
            state = state,
            onTabChanged = {
                val tab = if (it) HomeTab.CUSTOMER_TAB else HomeTab.SUPPLIER_TAB
                screenModel.pushIntent(HomeContract.Intent.OnTabChanged(tab))
            },
            onAvatarClicked = ::onBusinessClicked,
            onToolbarActionClicked = {},
            onPrimaryVpaClicked = {},
            onSearchClicked = {},
            onSortAndFilterClicked = {},
            onCustomerClicked = {},
            onSupplierClicked = {},
            onCustomerProfileClicked = {},
            onSupplierProfileClicked = {},
            onAddRelationshipClicked = {},
            onDynamicItemClicked = {_, _ ->},
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
        }
    }
}

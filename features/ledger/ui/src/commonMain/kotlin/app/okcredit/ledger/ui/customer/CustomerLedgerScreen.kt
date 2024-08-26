package app.okcredit.ledger.ui.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.ui.customer.CustomerLedgerContract.*
import app.okcredit.ledger.ui.customer.composable.CustomerLedgerUi
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

data class CustomerLedgerScreen(val customerId: String) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<CustomerLedgerModel>(CustomerLedgerModel::class)

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state, navigator)
    }

    @Composable
    private fun render(
        screenModel: CustomerLedgerModel,
        state: State,
        navigator: Navigator
    ) {
        CustomerLedgerUi(
            state = state,
            loadTransactions = {
                screenModel.pushIntent(
                    Intent.LoadTransactions(
                        showOldClicked = false,
                        customerId = customerId
                    )
                )
            },
            onProfileClicked = { },
            onBackClicked = { navigator.pop() },
            openMoreBottomSheet = { },
            onMenuOptionClicked = { },
            onLearnMoreClicked = { },
            onLoadMoreTransactionsClicked = {
                screenModel.pushIntent(
                    Intent.LoadTransactions(
                        showOldClicked = true,
                        customerId = customerId
                    )
                )
            },
            onTransactionClicked = { _, _, _ -> },
            trackOnRetryClicked = { _, _ -> },
            trackReceiptLoadFailed = { _, _ -> },
            trackNoInternetError = { _, _ -> },
            onTransactionShareButtonClicked = { },
            onReceivedClicked = { },
            onGivenClicked = { },
            onBalanceClicked = { },
            onWhatsappClicked = { },
            onCallClicked = { screenModel.pushIntent(Intent.OnCallClicked) },
            onErrorToastDismissed = {}
        )
    }

    private fun handleViewEvent(event: ViewEvent, navigator: Navigator) {
        when (event) {
            is ViewEvent.ShowError -> {}
        }
    }

}
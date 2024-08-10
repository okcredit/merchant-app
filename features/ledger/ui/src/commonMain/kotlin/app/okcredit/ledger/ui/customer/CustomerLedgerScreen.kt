package app.okcredit.ledger.ui.customer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.ui.customer.composable.LedgerUi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

@Inject
class CustomerLedgerScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<CustomerLedgerModel>(CustomerLedgerModel::class)

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state)
    }

    @Composable
    private fun render(
        screenModel: CustomerLedgerModel,
        state: CustomerLedgerContract.State
    ) {
        LedgerUi(
            state = state,
            onProfileClicked = { },
            onBackClicked = { },
            openMoreBottomSheet = { },
            onMenuOptionClicked = { },
            onLearnMoreClicked = { },
            onLoadMoreTransactionsClicked = { },
            onTransactionClicked = { _, _, _ -> },
            trackOnRetryClicked = { _, _ -> },
            trackReceiptLoadFailed = { _, _ -> },
            trackNoInternetError = { _, _ -> },
            onTransactionShareButtonClicked = { },
            onReceivedClicked = { },
            onGivenClicked = { },
            onBalanceClicked = { },
            onWhatsappClicked = { },
            onCallClicked = { },
        )
    }

    private fun handleViewEvent(event: CustomerLedgerContract.ViewEvent, navigator: Navigator) {
        when (event) {
            is CustomerLedgerContract.ViewEvent.ShowError -> {}
        }
    }

}
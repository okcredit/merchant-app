package app.okcredit.ledger.ui.supplier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.ui.supplier.composable.SupplierLedgerUi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

@Inject
class SupplierLedgerScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SupplierLedgerModel>(SupplierLedgerModel::class)

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state)
    }

    @Composable
    private fun render(screenModel: SupplierLedgerModel, state: SupplierLedgerContract.State) {
        SupplierLedgerUi(
            state = state,
            onProfileClicked = { },
            onBackClicked = { },
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
        )

    }

    private fun handleViewEvent(event: SupplierLedgerContract.ViewEvent, navigator: Navigator) {
        when (event) {
            is SupplierLedgerContract.ViewEvent.ShowError -> {}
        }
    }
}
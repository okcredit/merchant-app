package app.okcredit.ledger.ui.supplier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.ui.LedgerScreenRegistry
import app.okcredit.ledger.ui.supplier.composable.SupplierLedgerUi
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import okcredit.base.di.observeViewEvents
import okcredit.base.di.rememberScreenModel

data class SupplierLedgerScreen(
    val supplierId: String,
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SupplierLedgerModel>()

        val state by screenModel.states.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        screenModel.observeViewEvents {
            handleViewEvent(it, navigator)
        }

        render(screenModel, state, navigator)
    }

    @Composable
    private fun render(
        screenModel: SupplierLedgerModel,
        state: SupplierLedgerContract.State,
        navigator: Navigator,
    ) {
        SupplierLedgerUi(
            state = state,
            onProfileClicked = {
                val supplierId = state.supplierDetails?.id
                if (supplierId != null)
                    navigator.push(
                        ScreenRegistry.get(
                            LedgerScreenRegistry.AccountProfile(
                                accountId = supplierId,
                                accountType = AccountType.SUPPLIER
                            )
                        )
                    )
            },
            onLoadTransaction = {
                screenModel.pushIntent(
                    SupplierLedgerContract.Intent.LoadTransaction(
                        showOldClicked = false,
                        supplierId = supplierId,
                    ),
                )
            },
            onBackClicked = { navigator.pop() },
            onMenuOptionClicked = { },
            onLearnMoreClicked = { },
            onLoadMoreTransactionsClicked = {
                screenModel.pushIntent(
                    SupplierLedgerContract.Intent.LoadTransaction(
                        showOldClicked = true,
                        supplierId = supplierId,
                    ),
                )
            },
            onTransactionClicked = { _, _, _ -> },
            trackOnRetryClicked = { _, _ -> },
            trackReceiptLoadFailed = { _, _ -> },
            trackNoInternetError = { _, _ -> },
            onTransactionShareButtonClicked = { },
            onReceivedClicked = { },
            onGivenClicked = { },
            onShareReportClicked = { },
            onPayOnlineClicked = { },
            onCallClicked = { },
            onErrorToastDismissed = { screenModel.pushIntent(SupplierLedgerContract.Intent.OnErrorToastDismissed) },
        )
    }

    private fun handleViewEvent(event: SupplierLedgerContract.ViewEvent, navigator: Navigator) {
        when (event) {
            is SupplierLedgerContract.ViewEvent.ShowError -> {}
        }
    }
}

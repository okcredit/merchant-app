package app.okcredit.ledger.ui.supplier.composable

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.composable.LedgerToolBar
import app.okcredit.ledger.ui.composable.LedgerToolBarState
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract
import okcredit.base.units.Paisa

@Composable
fun SupplierLedgerUi(
    state: SupplierLedgerContract.State,
    onProfileClicked: (String) -> Unit,
    onBackClicked: () -> Unit,
    onMenuOptionClicked: (MenuOptions) -> Unit,
    onLearnMoreClicked: () -> Unit,
    onLoadMoreTransactionsClicked: () -> Unit,
    onTransactionClicked: (String, Paisa, Boolean) -> Unit,
    trackOnRetryClicked: (String, String) -> Unit,
    trackReceiptLoadFailed: (String, String) -> Unit,
    trackNoInternetError: (String, String) -> Unit,
    onTransactionShareButtonClicked: (String) -> Unit,
    onReceivedClicked: () -> Unit,
    onGivenClicked: () -> Unit,
    onShareReportClicked: () -> Unit,
    onPayOnlineClicked: () -> Unit,
    onCallClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            val customer = state.supplierDetails
            val toolbar = state.toolbarData
            LedgerToolBar(
                state = if (customer == null && toolbar == null)
                    null
                else LedgerToolBarState(
                    id = customer!!.id,
                    name = customer.name,
                    mobile = customer.mobile ?: "",
                    profileImage = customer.profileImage,
                    registered = customer.registered,
                    blocked = customer.blocked,
                    toolbarOptions = toolbar!!.toolbarOptions,
                    moreMenuOptions = toolbar.moreMenuOptions
                ),
                onProfileClicked = onProfileClicked,
                openMoreBottomSheet = {},
                onMenuOptionClicked = onMenuOptionClicked,
                onBackClicked = onBackClicked
            )
        },
        content = { paddingValues ->
            SupplierLedgerList(
                modifier = Modifier,
                paddingValues = paddingValues,
                ledgerItems = state.ledgerItems,
                transactionScrollPosition = state.transactionScrollPosition,
                onTransactionClicked = onTransactionClicked,
                onLoadMoreTransactionsClicked = onLoadMoreTransactionsClicked,
                trackOnRetryClicked = trackOnRetryClicked,
                trackReceiptLoadFailed = trackReceiptLoadFailed,
                trackNoInternetError = trackNoInternetError,
                onTransactionShareButtonClicked = onTransactionShareButtonClicked,
                onLearnMoreClicked = onLearnMoreClicked,
            )
        },
        bottomBar = {
            SupplierBottomUi(
                ledgerNotEmpty = state.ledgerItems.size > 1,
                onReceivedClicked = onReceivedClicked,
                onGivenClicked = onGivenClicked,
                canShowPayOnline = false,
                onCallClicked = onCallClicked,
                onPayOnlineClicked = onPayOnlineClicked,
                closingBalance = state.supplierDetails?.balance ?: Paisa.ZERO,
                onShareReportClicked = onShareReportClicked,
            )
        }
    )
}
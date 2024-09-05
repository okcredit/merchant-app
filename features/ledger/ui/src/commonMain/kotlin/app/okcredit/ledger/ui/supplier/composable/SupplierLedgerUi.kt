package app.okcredit.ledger.ui.supplier.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.composable.LedgerToolBar
import app.okcredit.ledger.ui.composable.LedgerToolBarState
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ledger.ui.supplier.SupplierLedgerContract
import app.okcredit.ui.composable.ErrorToast
import app.okcredit.ui.composable.shortToast
import com.dokar.sonner.rememberToasterState
import okcredit.base.units.Paisa

@Composable
fun SupplierLedgerUi(
    state: SupplierLedgerContract.State,
    onLoadTransaction: () -> Unit,
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
    onErrorToastDismissed: () -> Unit
) {
    val toastState = rememberToasterState { onErrorToastDismissed() }
    LaunchedEffect(state.errorMessage) {
        if (!state.errorMessage.isNullOrEmpty()) {
            toastState.shortToast(message = state.errorMessage)
        }
    }

    LaunchedEffect(true) {
        onLoadTransaction()
    }
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
            Box {
                SupplierBottomUi(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    ledgerNotEmpty = state.ledgerItems.size > 1,
                    onReceivedClicked = onReceivedClicked,
                    onGivenClicked = onGivenClicked,
                    canShowPayOnline = false,
                    onCallClicked = onCallClicked,
                    onPayOnlineClicked = onPayOnlineClicked,
                    closingBalance = state.supplierDetails?.balance ?: Paisa.ZERO,
                    onShareReportClicked = onShareReportClicked,
                )

                ErrorToast(state = toastState)
            }
        }
    )
}
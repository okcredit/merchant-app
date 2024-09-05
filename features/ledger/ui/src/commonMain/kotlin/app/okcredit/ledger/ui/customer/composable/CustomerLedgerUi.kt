package app.okcredit.ledger.ui.customer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.composable.LedgerToolBar
import app.okcredit.ledger.ui.composable.LedgerToolBarState
import app.okcredit.ledger.ui.customer.CustomerLedgerContract.State
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ui.composable.ErrorToast
import app.okcredit.ui.composable.shortToast
import com.dokar.sonner.rememberToasterState
import okcredit.base.units.Paisa

@Composable
fun CustomerLedgerUi(
    state: State,
    onProfileClicked: (String) -> Unit,
    loadTransactions: () -> Unit,
    onBackClicked: () -> Unit,
    openMoreBottomSheet: (Boolean) -> Unit,
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
    onBalanceClicked: () -> Unit,
    onCallClicked: () -> Unit,
    onWhatsappClicked: () -> Unit,
    onErrorToastDismissed: () -> Unit,
) {
    val toastState = rememberToasterState { onErrorToastDismissed() }
    LaunchedEffect(state.errorMessage) {
        if (!state.errorMessage.isNullOrEmpty()) {
            toastState.shortToast(message = state.errorMessage)
        }
    }

    LaunchedEffect(true) {
        loadTransactions()
    }

    Scaffold(
        topBar = {
            val customer = state.customerDetails
            val toolbar = state.toolbarData
            LedgerToolBar(
                state = if (customer == null || toolbar == null) {
                    null
                } else {
                    LedgerToolBarState(
                        id = customer.id,
                        name = customer.name,
                        mobile = customer.mobile ?: "",
                        profileImage = customer.profileImage,
                        registered = customer.registered,
                        blocked = customer.blocked,
                        toolbarOptions = toolbar.toolbarOptions,
                        moreMenuOptions = toolbar.moreMenuOptions,
                    )
                },
                onProfileClicked = onProfileClicked,
                openMoreBottomSheet = openMoreBottomSheet,
                onMenuOptionClicked = onMenuOptionClicked,
                onBackClicked = onBackClicked,
            )
        },
        content = { paddingValues ->
            CustomerLedgerList(
                paddingValues = paddingValues,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.surface),
                ledgerItems = state.ledgerItems,
                transactionScrollPosition = state.transactionScrollPosition,
                onLearnMoreClicked = onLearnMoreClicked,
                onLoadMoreTransactionsClicked = onLoadMoreTransactionsClicked,
                onTransactionShareButtonClicked = onTransactionShareButtonClicked,
                onTransactionClicked = onTransactionClicked,
                trackReceiptLoadFailed = trackReceiptLoadFailed,
                trackOnRetryClicked = trackOnRetryClicked,
                trackNoInternetError = trackNoInternetError,
            )
        },
        bottomBar = {
            Box(
                contentAlignment = Alignment.BottomCenter,
            ) {
                CustomerBottomUi(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    dueDate = state.customerDetails?.formattedDueDate,
                    balance = state.customerDetails?.balance,
                    onMoreClicked = { openMoreBottomSheet(true) },
                    onGivenClicked = onGivenClicked,
                    onReceivedClicked = onReceivedClicked,
                    onBalanceClicked = onBalanceClicked,
                    onWhatsappClicked = onWhatsappClicked,
                    onCallClicked = onCallClicked,
                )

                ErrorToast(state = toastState)
            }
        },
    )
}

package app.okcredit.ledger.ui.supplier.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import app.okcredit.ledger.ui.composable.LedgerDateView
import app.okcredit.ledger.ui.composable.LedgerEmptyPlaceHolder
import app.okcredit.ledger.ui.composable.LedgerLoadMoreView
import app.okcredit.ledger.ui.composable.LedgerLoadingShimmerView
import app.okcredit.ledger.ui.composable.LedgerTransactionView
import app.okcredit.ledger.ui.composable.TransactionViewState
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.model.LedgerItem
import okcredit.base.units.Paisa

@Composable
fun SupplierLedgerList(
    paddingValues: PaddingValues,
    modifier: Modifier,
    ledgerItems: List<LedgerItem>,
    transactionScrollPosition: Int?,
    onLearnMoreClicked: () -> Unit,
    onLoadMoreTransactionsClicked: () -> Unit,
    onTransactionClicked: (String, Paisa, Boolean) -> Unit,
    trackOnRetryClicked: (String, String) -> Unit,
    trackReceiptLoadFailed: (String, String) -> Unit,
    trackNoInternetError: (String, String) -> Unit,
    onTransactionShareButtonClicked: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(ledgerItems.size) {
        if (transactionScrollPosition == null) {
            scrollState.scrollToItem(index = ledgerItems.size, scrollOffset = 0)
        } else {
            scrollState.scrollToItem(index = transactionScrollPosition)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = paddingValues,
        state = scrollState,
    ) {
        if (ledgerItems.isEmpty()) {
            item(0) {
                LedgerLoadingShimmerView()
            }
        }
        items(ledgerItems.size) { index ->
            when (val item = ledgerItems[index]) {
                is LedgerItem.EmptyPlaceHolder -> LedgerEmptyPlaceHolder(
                    accountType = AccountType.Customer,
                    onLearnMoreClicked = onLearnMoreClicked,
                )

                is LedgerItem.LoadMoreItem -> LedgerLoadMoreView {
                    onLoadMoreTransactionsClicked()
                }

                is LedgerItem.DateItem -> LedgerDateView(date = item.date)

                is LedgerItem.TransactionItem -> LedgerTransactionView(
                    modifier = Modifier,
                    item = TransactionViewState(
                        txnId = item.txnId,
                        relationshipId = item.relationshipId,
                        createdBySelf = item.createdBySelf,
                        isDiscountTransaction = item.isDiscountTransaction,
                        txnGravity = item.txnGravity,
                        closingBalance = item.closingBalance,
                        amount = item.amount,
                        date = item.date,
                        dirty = item.dirty,
                        txnTag = item.txnTag,
                        txnType = item.txnType,
                        note = item.note,
                        imageCount = item.imageCount,
                        image = item.image,
                        accountType = item.accountType,
                        collectionId = item.collectionId,
                    ),
                    isLastItem = index == ledgerItems.lastIndex,
                    onTransactionClicked = onTransactionClicked,
                    trackOnRetryClicked = trackOnRetryClicked,
                    trackReceiptLoadFailed = trackReceiptLoadFailed,
                    trackNoInternetError = trackNoInternetError,
                    onTransactionShareButtonClicked = onTransactionShareButtonClicked,
                )
            }
        }
    }
}
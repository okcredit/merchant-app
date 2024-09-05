package app.okcredit.ledger.ui.model

import app.okcredit.ledger.ui.composable.TxnGravity
import app.okcredit.ledger.ui.composable.UiTxnStatus
import okcredit.base.units.Paisa

sealed class LedgerItem {

    data class EmptyPlaceHolder(val name: String?) : LedgerItem()

    data class DateItem(val date: String) : LedgerItem()

    data object LoadMoreItem : LedgerItem()

    data class TransactionItem(
        val txnId: String,
        val collectionId: String?,
        val relationshipId: String,
        val createdBySelf: Boolean = true,
        val imageCount: Int = 0,
        val image: String? = "",
        val isDiscountTransaction: Boolean = false,
        val txnGravity: TxnGravity,
        val closingBalance: Paisa,
        val amount: Paisa,
        val date: String,
        val dirty: Boolean,
        val txnTag: String?,
        val note: String?,
        val txnType: UiTxnStatus = UiTxnStatus.Transaction,
        val accountType: AccountType = AccountType.Customer,
    ) : LedgerItem()
}
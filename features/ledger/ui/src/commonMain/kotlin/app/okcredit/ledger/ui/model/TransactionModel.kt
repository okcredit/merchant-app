package app.okcredit.ledger.ui.model

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.Transaction
import okcredit.base.units.Paisa
import tech.okcredit.collection.model.OnlinePayment


data class TransactionData(
    val transactions: List<TransactionDueInfo>,
    val lastIndexOfZeroBalanceDue: Int,
)

data class TransactionDueInfo(
    val transaction: Transaction,
    val currentDue: Paisa,
    val onlinePayment: OnlinePayment?,
    val account: Account? = null
) {
    val isOnlineTransaction: Boolean
        get() = onlinePayment != null
}

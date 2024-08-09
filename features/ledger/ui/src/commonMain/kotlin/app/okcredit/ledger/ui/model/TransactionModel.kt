package app.okcredit.ledger.ui.model

import app.okcredit.ledger.contract.model.Transaction
import okcredit.base.units.Paisa


data class TransactionData(
    val transactions: List<TransactionDueInfo>,
    val lastIndexOfZeroBalanceDue: Int,
)

data class TransactionDueInfo(
    val transaction: Transaction,
    val currentDue: Paisa,
)

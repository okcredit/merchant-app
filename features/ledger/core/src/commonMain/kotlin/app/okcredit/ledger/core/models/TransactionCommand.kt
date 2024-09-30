package app.okcredit.ledger.core.models

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import kotlinx.serialization.Serializable
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp

@Serializable
sealed class TransactionCommand {
    abstract val id: String
    abstract val transactionId: String
    abstract val accountType: AccountType
    abstract val createTime: Timestamp
}

@Serializable
data class CreateTransaction(
    override val id: String,
    override val transactionId: String,
    override val accountType: AccountType,
    override val createTime: Timestamp,
    val accountId: String,
    val amount: Paisa,
    val type: Transaction.Type,
    val note: String?,
    val billDate: Timestamp,
    val bills: List<String>,
) : TransactionCommand()

@Serializable
data class UpdateTransactionAmount(
    override val id: String,
    override val transactionId: String,
    override val accountType: AccountType,
    override val createTime: Timestamp,
    val amount: Paisa,
) : TransactionCommand()

@Serializable
data class UpdateTransactionNote(
    override val id: String,
    override val transactionId: String,
    override val accountType: AccountType,
    override val createTime: Timestamp,
    val note: String?,
) : TransactionCommand()

@Serializable
data class DeleteTransaction(
    override val id: String,
    override val transactionId: String,
    override val accountType: AccountType,
    override val createTime: Timestamp,
) : TransactionCommand()

data class TransactionSummary(
    val currentDue: Long,
    val lastIndexOfZeroBalanceDue: Int?,
)

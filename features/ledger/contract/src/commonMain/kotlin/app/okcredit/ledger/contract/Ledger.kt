package app.okcredit.ledger.contract

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.model.UpdateTransactionRequest
import okcredit.base.randomUUID
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp

interface Ledger {

    suspend fun recordTransaction(
        accountId: String,
        accountType: AccountType,
        amount: Paisa,
        type: Transaction.Type,
        billDate: Timestamp,
        transactionId: String = randomUUID(),
        note: String? = null,
        bills: List<String> = emptyList(),
    ): Transaction

    suspend fun addAccount(
        name: String,
        mobile: String?,
        accountType: AccountType,
    ): Account

    suspend fun updateTransaction(
        request: UpdateTransactionRequest,
    )

    suspend fun deleteTransaction(
        transactionId: String,
    )

    suspend fun deleteAccount(
        accountId: String,
        accountType: AccountType,
    )
}

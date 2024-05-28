package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import kotlinx.coroutines.flow.Flow

interface GetTransactionsForAccount {

    fun execute(accountId: String, accountType: AccountType): Flow<List<Transaction>>
}
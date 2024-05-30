package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import kotlinx.coroutines.flow.Flow


interface GetAccounts {

    /**
     * Fetches all accounts from the ledger.
     *
     * @param sortBy The field to sort the accounts by.
     * @param accountType The type of account to filter by. If null, then both supplier and customer are returned
     * @param limit The maximum number of accounts to return.
     * @param offset The number of accounts to skip.
     */
    fun execute(
        sortBy: SortBy,
        accountType: AccountType? = null,
        limit: Int = 5_000,
        offset: Int = 0,
    ): Flow<List<Account>>
}

enum class SortBy {
    NAME,
    LAST_ACTIVITY,
    LAST_PAYMENT,
    BALANCE_DUE,
}

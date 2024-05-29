package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import kotlinx.coroutines.flow.Flow

interface GetAccounts {

    /**
     * Fetches all accounts sorted by [sortBy].
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

package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import kotlinx.coroutines.flow.Flow

interface GetAccount {
    /**
     * Fetches details of an account from the ledger.
     * @param accountId: The account id for which account is to be fetched.
     * @param accountType: The type of account to filter by.
     *
     * @return Account: The account details. Will be null if account is not found.
     */
    fun execute(accountId: String, accountType: AccountType): Flow<Account?>
}

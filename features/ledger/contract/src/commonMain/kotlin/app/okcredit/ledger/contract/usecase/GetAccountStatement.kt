package app.okcredit.ledger.contract.usecase

import app.okcredit.ledger.contract.model.Transaction
import kotlinx.coroutines.flow.Flow
import okcredit.base.units.Timestamp


interface GetAccountStatement {

    /**
     * Get transactions for a given account.
     * @param accountId: The account id for which transactions are to be fetched.
     * @param startTime: The start time for the transactions. If not provided, fetches transactions from 0.
     * @param endTime: The end time for the transactions. If not provided, fetches all transactions. Including any new transactions added after the call.
     */
    fun execute(
        accountId: String,
        startTime: Timestamp? = null,
        endTime: Timestamp? = null,
    ): Flow<List<Transaction>>
}

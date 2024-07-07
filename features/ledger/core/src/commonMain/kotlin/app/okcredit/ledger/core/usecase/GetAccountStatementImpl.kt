package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.usecase.GetAccountStatement
import app.okcredit.ledger.core.local.LedgerLocalSource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Timestamp
import okcredit.base.units.ZERO_TIMESTAMP

@Inject
class GetAccountStatementImpl(
    private val localSource: LedgerLocalSource,
) : GetAccountStatement {

    override fun execute(
        accountId: String,
        startTime: Timestamp?,
        endTime: Timestamp?,
    ): Flow<List<Transaction>> {
        return localSource.getTransactionsForAccount(
            accountId = accountId,
            startTime = startTime ?: ZERO_TIMESTAMP,
            endTime = endTime,
        )
    }
}

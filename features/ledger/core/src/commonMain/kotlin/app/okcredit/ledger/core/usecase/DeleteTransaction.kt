package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.models.DeleteTransaction
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import okcredit.base.units.timestamp

@Inject
class DeleteTransaction(
    private val ledgerLocalSource: LedgerLocalSource,
    private val ledgerSyncManager: LedgerSyncManager,
) {

    suspend fun execute(transactionId: String, accountType: AccountType) {
        val transaction = ledgerLocalSource.getTransactionDetails(transactionId).firstOrNull()
            ?: throw IllegalArgumentException("Transaction not found")

        ledgerLocalSource.deleteTransaction(
            command = DeleteTransaction(
                id = randomUUID(),
                transactionId = transactionId,
                accountType = accountType,
                createTime = Clock.System.now().timestamp,
            ),
            transaction = transaction,
        )

        ledgerSyncManager.scheduleTransactionSync(
            businessId = transaction.businessId,
            source = "DELETE_TRANSACTION",
        )
    }
}

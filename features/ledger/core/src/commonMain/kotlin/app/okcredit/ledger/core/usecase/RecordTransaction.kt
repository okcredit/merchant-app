package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.usecase.InvalidAmountError
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.models.CreateTransaction
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class RecordTransaction(
    getActiveBusinessIdLazy: Lazy<GetActiveBusinessId>,
    ledgerLocalSourceLazy: Lazy<LedgerLocalSource>,
    ledgerSyncManagerLazy: Lazy<LedgerSyncManager>,
) {

    private val getActiveBusinessId by lazy { getActiveBusinessIdLazy.value }
    private val ledgerLocalSource by lazy { ledgerLocalSourceLazy.value }
    private val ledgerSyncManager by lazy { ledgerSyncManagerLazy.value }

    suspend fun execute(
        accountId: String,
        accountType: AccountType,
        amount: Paisa,
        type: Transaction.Type,
        billDate: Timestamp,
        transactionId: String,
        note: String? = null,
        bills: List<String> = emptyList(),
    ): Transaction {
        requireValidAmount(amount)

        val businessId = getActiveBusinessId.execute()
        val createTime = Clock.System.now().timestamp

        val command = CreateTransaction(
            id = randomUUID(),
            transactionId = transactionId,
            accountId = accountId,
            amount = amount,
            type = type,
            note = note,
            billDate = billDate,
            bills = bills,
            accountType = accountType,
            createTime = createTime,
        )

        val transaction = Transaction(
            id = transactionId,
            businessId = businessId,
            accountId = accountId,
            amount = amount,
            dirty = true,
            type = type,
            note = note,
            billDate = billDate,
            createdAt = createTime,
        )

        ledgerLocalSource.addTransaction(
            businessId = businessId,
            command = command,
            transaction = transaction,
        )

        ledgerSyncManager.scheduleTransactionSync(
            businessId = businessId,
            source = "record_transaction",
        )

        return transaction
    }

    private fun requireValidAmount(amount: Paisa) {
        if (amount == 0.paisa) {
            throw InvalidAmountError()
        }
    }
}

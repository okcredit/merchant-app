package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.ApiTransaction
import app.okcredit.ledger.core.remote.models.ROLE_BUYER
import app.okcredit.ledger.core.remote.models.TYPE_LIST
import kotlinx.coroutines.delay
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class SyncTransactions(
    private val syncTransactionCommand: SyncTransactionCommand,
    private val getActiveBusinessId: GetActiveBusinessId,
    private val localSource: LedgerLocalSource,
    private val remoteSource: LedgerRemoteSource,
) {

    suspend fun execute(businessId: String? = null) {
        val final = businessId ?: getActiveBusinessId.execute()

        // Sync transaction commands
        syncTransactionCommand.execute()

        // explicit 250ms (SLA on backend) delay added as sync transaction is async on backend and new command have not been processed yet.
        delay(250)

        // Fetch transactions from server
        fetchTransactionsFromServer(final)
    }

    private suspend fun fetchTransactionsFromServer(businessId: String) {
        val lastUpdated = localSource.getLastUpdatedTransactionTimestamp(businessId)
        val response = remoteSource.getTransactions(
            businessId = businessId,
            startDate = lastUpdated.epochMillis,
            source = "ledger",
        )

        if (response?.type == TYPE_LIST) {
            response.listData?.transactions?.let {
                saveTransactionsIntoDatabase(
                    transactions = it,
                    businessId = businessId,
                )
            }
        }
    }

    private suspend fun saveTransactionsIntoDatabase(
        transactions: List<ApiTransaction>,
        businessId: String
    ) {
        if (transactions.isEmpty()) return

        localSource.insertTransactions(transactions.toDomainTransactions(businessId))
        // Recalculate customer summary after inserting transactions
        val customers = transactions.mapNotNull { it.accountId }.distinct()
        if (customers.isNotEmpty()) {
            localSource.recalculateCustomerSummary(customers)
        }
    }
}

private fun List<ApiTransaction>.toDomainTransactions(businessId: String): List<Transaction> {
    return this.map {
        Transaction(
            businessId = businessId,
            id = it.id!!,
            accountId = it.accountId!!,
            amount = it.amount!!.paisa,
            type = Transaction.Type.fromCode(it.type!!),
            createdByCustomer = it.creatorRole == ROLE_BUYER,
            deletedByCustomer = it.deleterRole == ROLE_BUYER,
            createdAt = it.createTimeMs!!.timestamp,
            note = it.note,
            billDate = it.billDate?.timestamp ?: 0L.timestamp,
            state = Transaction.State.getTransactionState(it.transactionState!!),
            category = Transaction.Category.getTransactionCategory(it.txCategory!!),
            dirty = false,
            updatedAt = it.updateTimeMs?.timestamp,
            deleted = it.deleted ?: false,
            deleteTime = it.deleteTimeMs?.timestamp,
            amountUpdated = it.amountUpdated ?: false,
            amountUpdateTime = it.amountUpdatedAt?.timestamp,
            referenceId = it.referenceId,
            referenceSource = it.referenceSource ?: 0,
        )
    }
}
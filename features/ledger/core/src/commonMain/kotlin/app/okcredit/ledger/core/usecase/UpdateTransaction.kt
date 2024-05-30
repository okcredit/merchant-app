package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.UpdateTransactionRequest
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.models.UpdateTransactionAmount
import app.okcredit.ledger.core.models.UpdateTransactionNote
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import okcredit.base.randomUUID
import okcredit.base.units.timestamp

class UpdateTransaction(
    private val ledgerLocalSource: LedgerLocalSource,
    private val ledgerSyncManager: LedgerSyncManager,
) {

    suspend fun execute(updateTransactionRequest: UpdateTransactionRequest) {
        when (updateTransactionRequest) {
            is UpdateTransactionRequest.UpdateAmount -> updateTransactionAmount(updateTransactionRequest)
            is UpdateTransactionRequest.UpdateNote -> updateTransactionNote(updateTransactionRequest)
        }
    }

    private suspend fun updateTransactionNote(updateTransactionRequest: UpdateTransactionRequest.UpdateNote) {
        val transaction = ledgerLocalSource.getTransactionDetails(updateTransactionRequest.transactionId).firstOrNull()
            ?: throw IllegalArgumentException("Transaction not found")

        if (!transaction.createdByMerchant) {
            throw IllegalArgumentException("Cannot update note for non merchant transaction")
        }

        if (transaction.note == updateTransactionRequest.note) {
            return
        }

        if (transaction.deleted) {
            throw IllegalArgumentException("Cannot update note for deleted transaction")
        }

        ledgerLocalSource.updateTransactionNote(
            businessId = transaction.businessId,
            command = UpdateTransactionNote(
                id = randomUUID(),
                transactionId = updateTransactionRequest.transactionId,
                accountType = updateTransactionRequest.accountType,
                note = updateTransactionRequest.note,
                createTime = Clock.System.now().timestamp,
            ),
        )

        ledgerSyncManager.scheduleTransactionSync(
            businessId = transaction.businessId,
            source = "UPDATE_TRANSACTION_NOTE"
        )
    }

    private suspend fun updateTransactionAmount(updateTransactionRequest: UpdateTransactionRequest.UpdateAmount) {
        val transaction = ledgerLocalSource.getTransactionDetails(updateTransactionRequest.transactionId).firstOrNull()
            ?: throw IllegalArgumentException("Transaction not found")

        if (!transaction.createdByMerchant) {
            throw IllegalArgumentException("Cannot update note for non merchant transaction")
        }

        if (transaction.amount == updateTransactionRequest.amount) {
            return
        }

        if (transaction.deleted) {
            throw IllegalArgumentException("Cannot update amount for deleted transaction")
        }

        ledgerLocalSource.updateTransactionAmount(
            businessId = transaction.businessId,
            command = UpdateTransactionAmount(
                id = randomUUID(),
                transactionId = updateTransactionRequest.transactionId,
                accountType = updateTransactionRequest.accountType,
                amount = updateTransactionRequest.amount,
                createTime = Clock.System.now().timestamp,
            ),
            existingAmount = transaction.amount,
            accountId = transaction.accountId,
            transactionType = transaction.type,
        )
    }
}
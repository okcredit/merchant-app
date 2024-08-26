package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.models.CreateTransaction
import app.okcredit.ledger.core.models.DeleteTransaction
import app.okcredit.ledger.core.models.TransactionCommand
import app.okcredit.ledger.core.models.UpdateTransactionAmount
import app.okcredit.ledger.core.models.UpdateTransactionNote
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.ApiCommandType
import app.okcredit.ledger.core.remote.models.ApiTransaction
import app.okcredit.ledger.core.remote.models.ApiTransactionCommand
import app.okcredit.ledger.core.remote.models.SyncTransactionRequest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import okcredit.base.network.ApiError
import okcredit.base.randomUUID
import okcredit.base.utils.withRetry
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
@Singleton
class SyncTransactionCommand(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val localSource: LedgerLocalSource,
    private val remoteSource: LedgerRemoteSource,
) {

    companion object {
        const val MAX_COMMANDS_TO_SYNC = 10
    }

    private val mutex = Mutex()

    suspend fun execute() {
        mutex.withLock {
            val businessId = getActiveBusinessId.execute()
            var commands = localSource.getTransactionCommands(businessId, MAX_COMMANDS_TO_SYNC)
            while (commands.isNotEmpty()) {
                pushCommandsToServer(commands, businessId)
                commands = localSource.getTransactionCommands(businessId, MAX_COMMANDS_TO_SYNC)
            }
        }
    }

    private suspend fun pushCommandsToServer(
        commands: List<TransactionCommand>,
        businessId: String,
    ) {
        withRetry(
            times = 3,
            shouldRetry = { it is ApiError || it is IOException },
        ) {
            val response = remoteSource.syncTransactions(
                request = SyncTransactionRequest(
                    operations = commands.map { it.toApiTransactionCommand() },
                ),
                businessId = businessId,
                flowId = randomUUID(),
            )

            val successCommandIdsList = response?.operationResponses
                ?.filter { it.status == 1 }
                ?.map { item -> item.id }

            if (successCommandIdsList.isNullOrEmpty()) {
                throw IllegalStateException("No successful commands found")
            }

            val successfulCommands = commands.filter { it.id in successCommandIdsList }
            localSource.markTransactionsSynced(successfulCommands)
        }
    }
}

fun TransactionCommand.toApiTransactionCommand(): ApiTransactionCommand {
    val apiCommandType = apiCommandType()
    return when (this) {
        is CreateTransaction -> {
            ApiTransactionCommand(
                id = this.id,
                transactionId = transactionId,
                type = apiCommandType.type.value,
                path = apiCommandType.path,
                transaction = getTransactionValue(this),
                timestamp = this.createTime.epochMillis,
                mask = apiCommandType.mask,
            )
        }

        is DeleteTransaction -> {
            ApiTransactionCommand(
                id = this.id,
                transactionId = transactionId,
                type = apiCommandType.type.value,
                path = apiCommandType.path,
                transaction = getTransactionValue(this),
                timestamp = this.createTime.epochMillis,
                mask = apiCommandType.mask,
            )
        }

        is UpdateTransactionAmount -> {
            ApiTransactionCommand(
                id = this.id,
                transactionId = transactionId,
                type = apiCommandType.type.value,
                path = apiCommandType.path,
                transaction = getTransactionValue(this),
                timestamp = this.createTime.epochMillis,
                mask = apiCommandType.mask,
            )
        }

        is UpdateTransactionNote -> {
            ApiTransactionCommand(
                id = this.id,
                transactionId = transactionId,
                type = apiCommandType.type.value,
                path = apiCommandType.path,
                transaction = getTransactionValue(this),
                timestamp = this.createTime.epochMillis,
                mask = apiCommandType.mask,
            )
        }
    }
}

private fun TransactionCommand.apiCommandType(): ApiCommandType {
    return when (this) {
        is CreateTransaction -> ApiCommandType.CREATE_TRANSACTION
        is DeleteTransaction -> ApiCommandType.DELETE_TRANSACTION
        is UpdateTransactionAmount -> ApiCommandType.UPDATE_TRANSACTION_AMOUNT
        is UpdateTransactionNote -> ApiCommandType.UPDATE_TRANSACTION_NOTE
    }
}

private fun getTransactionValue(command: TransactionCommand): ApiTransaction {
    return when (command) {
        is CreateTransaction -> ApiTransaction(
            id = command.transactionId,
            accountId = command.accountId,
            type = command.type.code,
            amount = command.amount.value,
            creatorRole = 1,
            createTimeMs = command.createTime.epochMillis,
            note = command.note,
            images = null,
            billDate = command.billDate.epochMillis,
            transactionState = Transaction.State.CREATED.code,
            createTime = command.createTime.epochMillis,
            deleted = false,
            txCategory = Transaction.Category.DEFAULT.code,
        )

        is UpdateTransactionNote -> ApiTransaction(
            note = command.note,
        )

        is DeleteTransaction -> ApiTransaction(
            transactionState = Transaction.State.DELETED.code,
            deleterRole = 1,
        )

        is UpdateTransactionAmount -> ApiTransaction(
            amount = command.amount.value,
        )
    }
}

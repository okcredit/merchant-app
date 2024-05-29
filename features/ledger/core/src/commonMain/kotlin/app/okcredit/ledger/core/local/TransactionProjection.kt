package app.okcredit.ledger.core.local

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.core.models.CreateTransaction
import app.okcredit.ledger.core.models.DeleteTransaction
import app.okcredit.ledger.core.models.TransactionCommand
import app.okcredit.ledger.core.models.UpdateTransactionAmount
import app.okcredit.ledger.core.models.UpdateTransactionNote
import app.okcredit.ledger.local.LedgerDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okcredit.base.appDispatchers
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import app.okcredit.ledger.contract.model.Transaction as DomainTransaction

class TransactionProjection(
    private val database: LedgerDatabase,
) {

    private val transactionQueries by lazy { database.transactionQueries }

    private val accountQueries by lazy { database.accountQueries }

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
        }
    }

    suspend fun addTransaction(
        businessId: String,
        command: CreateTransaction,
        transaction: DomainTransaction,
    ) {
        withContext(appDispatchers.io) {
            database.transaction {
                transactionQueries.insertTransaction(
                    transaction.toDbTransaction(command.accountType),
                )
                transactionQueries.insertTransactionCommand(
                    businessId = businessId,
                    transactionId = transaction.id,
                    data_ = json.encodeToString(command),
                    accountType = command.accountType,
                    commandId = command.id,
                    commandType = CommandType.CREATE,
                )
                if (command.accountType == app.okcredit.ledger.contract.model.AccountType.CUSTOMER) {
                    accountQueries.updateCustomerSummaryForTxnCreated(
                        amount = transaction.amount,
                        customerId = transaction.accountId,
                        createdAt = transaction.createdAt,
                        category = transaction.category.code.toLong(),
                        state = transaction.state.code.toLong(),
                        type = if (transaction.type == DomainTransaction.Type.CREDIT) 1 else 0,
                    )
                } else {
                    accountQueries.updateSupplierSummaryForTxnCreated(
                        amount = transaction.amount,
                        supplierId = transaction.accountId,
                        createdAt = transaction.createdAt,
                        category = transaction.category.code.toLong(),
                        state = transaction.state.code.toLong(),
                        type = if (transaction.type == DomainTransaction.Type.CREDIT) 1 else 0,
                    )
                }
            }
        }
    }

    suspend fun deleteTransaction(
        command: DeleteTransaction,
        transaction: DomainTransaction,
        businessId: String,
    ) {
        withContext(appDispatchers.io) {
            database.transaction {
                transactionQueries.insertTransactionCommand(
                    commandId = command.id,
                    businessId = businessId,
                    transactionId = command.transactionId,
                    data_ = json.encodeToString(command),
                    accountType = command.accountType,
                    commandType = CommandType.DELETE,
                )
                transactionQueries.markTransactionDeleted(
                    deleteTime = transaction.deleteTime,
                    id = command.transactionId,
                )
                accountQueries.updateCustomerSummaryForTxnDeleted(
                    deleteTime = transaction.deleteTime!!,
                    amount = transaction.amount,
                    category = transaction.category.code.toLong(),
                    customerId = transaction.accountId,
                    type = if (transaction.type == DomainTransaction.Type.CREDIT) 1 else 0,
                )
            }
        }
    }

    suspend fun updateTransactionAmount(
        command: UpdateTransactionAmount,
        existingAmount: Paisa,
        transaction: DomainTransaction,
        businessId: String,
    ) {
        withContext(appDispatchers.io) {
            database.transaction {
                transactionQueries.insertTransactionCommand(
                    commandId = command.id,
                    businessId = businessId,
                    transactionId = command.transactionId,
                    data_ = json.encodeToString(command),
                    accountType = command.accountType,
                    commandType = CommandType.UPDATE_AMOUNT,
                )
                transactionQueries.updateTransactionAmount(
                    id = command.transactionId,
                    amount = transaction.amount,
                    amountUpdatedTime = transaction.amountUpdateTime,
                )
                if (command.accountType == app.okcredit.ledger.contract.model.AccountType.CUSTOMER) {
                    accountQueries.updateCustomerSummaryForTxnAmountUpdated(
                        customerId = transaction.accountId,
                        amountUpdatedAt = transaction.deleteTime!!,
                        newAmount = transaction.amount.value,
                        existingAmount = existingAmount,
                        type = if (transaction.type == DomainTransaction.Type.CREDIT) 1 else 0,
                    )
                } else {
                    accountQueries.updateSupplierSummaryForTxnAmountUpdated(
                        supplierId = transaction.accountId,
                        amountUpdatedAt = transaction.deleteTime!!,
                        newAmount = transaction.amount.value,
                        existingAmount = existingAmount,
                        type = if (transaction.type == DomainTransaction.Type.CREDIT) 1 else 0,
                    )
                }
            }
        }
    }

    suspend fun updateTransactionNote(
        command: UpdateTransactionNote,
        businessId: String,
    ) {
        withContext(appDispatchers.io) {
            database.transaction {
                transactionQueries.insertTransactionCommand(
                    commandId = command.id,
                    businessId = businessId,
                    transactionId = command.transactionId,
                    data_ = json.encodeToString(command),
                    accountType = command.accountType,
                    commandType = CommandType.UPDATE_NOTE,
                )
                transactionQueries.updateTransactionNote(
                    id = command.transactionId,
                    note = command.note,
                )
            }
        }
    }

    fun getTransactionDetails(transactionId: String): Flow<DomainTransaction?> {
        return transactionQueries.transactionById(transactionId)
            .mapToDomain {
                it?.toDomainTransaction()
            }
    }

    fun getTransactionsForAccount(
        accountId: String,
    ): Flow<List<DomainTransaction>> {
        return transactionQueries.transactionsForAccount(accountId = accountId)
            .mapToDomainList {
                it.toDomainTransaction()
            }
    }

    suspend fun getTransactionCommands(businessId: String, limit: Int): List<TransactionCommand> {
        return withContext(appDispatchers.io) {
            transactionQueries.transactionCommands(businessId, limit.toLong())
                .executeAsList()
                .map {
                    when (it.commandType) {
                        CommandType.CREATE -> json.decodeFromString<CreateTransaction>(it.data_)
                        CommandType.DELETE -> json.decodeFromString<DeleteTransaction>(it.data_)
                        CommandType.UPDATE_AMOUNT -> json.decodeFromString<UpdateTransactionAmount>(it.data_)
                        CommandType.UPDATE_NOTE -> json.decodeFromString<UpdateTransactionNote>(it.data_)
                    }
                }
        }
    }

    suspend fun markTransactionsSynced(commands: List<TransactionCommand>) {
        withContext(appDispatchers.io) {
            commands.forEach {
                transactionQueries.deleteTransactionCommand(commandId = it.id)
                val commandExists = transactionQueries.commandExistForTransaction(it.transactionId).executeAsOne()
                if (!commandExists) {
                    transactionQueries.markTransactionSynced(it.transactionId)
                }
            }
        }
    }

    suspend fun getLastUpdatedTransactionTimestamp(businessId: String): Timestamp {
        return withContext(appDispatchers.io) {
            transactionQueries.maxUpdatedAt(businessId).executeAsOneOrNull()?.MAX ?: Timestamp(0)
        }
    }

    suspend fun insertTransactions(transactions: List<DomainTransaction>) {
        withContext(appDispatchers.io) {
            database.transaction {
                transactions.forEach {
                    transactionQueries.insertTransaction(it.toDbTransaction(accountType = AccountType.CUSTOMER))
                }
            }
        }
    }
}

fun Transaction.toDomainTransaction(): DomainTransaction {
    return DomainTransaction(
        businessId = this.businessId,
        id = this.id,
        accountId = this.accountId,
        amount = this.amount,
        type = this.type,
        note = this.note,
        billDate = this.billDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        referenceSource = this.referenceSource,
        state = this.state,
        category = this.category,
        createdByCustomer = this.createdByCustomer,
        deletedByCustomer = this.deletedByCustomer,
        amountUpdated = this.amountUpdated,
        amountUpdateTime = this.amountUpdatedTime,
        referenceId = this.referenceId,
        deleted = this.deleted,
        deleteTime = this.deleteTime,
        dirty = this.dirty,
    )
}

enum class CommandType {
    CREATE,
    DELETE,
    UPDATE_AMOUNT,
    UPDATE_NOTE,
}

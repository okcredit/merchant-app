package app.okcredit.ledger.core.local

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.db.SqlDriver
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.core.models.CreateTransaction
import app.okcredit.ledger.core.models.DeleteTransaction
import app.okcredit.ledger.core.models.UpdateTransactionAmount
import app.okcredit.ledger.core.models.UpdateTransactionNote
import app.okcredit.ledger.local.LedgerDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okcredit.base.appDispatchers
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.timestamp
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import app.okcredit.ledger.contract.model.Transaction as DomainTransaction

typealias LedgerSqlDriver = SqlDriver

@Inject
@SingleIn(AppScope::class)
class LedgerLocalSource(driver: Lazy<LedgerSqlDriver>) {

    private val database by lazy {
        LedgerDatabase(
            driver = driver.value,
            AccountAdapter = Account.Adapter(
                typeAdapter = AccountTypeAdapter,
                createdAtAdapter = TimestampAdapter,
                updatedAtAdapter = TimestampAdapter,
                statusAdapter = AccountStatusAdapter,
            ),
            TransactionAdapter = Transaction.Adapter(
                createdAtAdapter = TimestampAdapter,
                updatedAtAdapter = TimestampAdapter,
                amountAdapter = PaisaAdapter,
                typeAdapter = TransactionTypeAdapter,
                amountUpdatedTimeAdapter = TimestampAdapter,
                billDateAdapter = TimestampAdapter,
                deleteTimeAdapter = TimestampAdapter,
                referenceSourceAdapter = IntAdapter,
                stateAdapter = TransactionStateAdapter,
                categoryAdapter = TransactionCategoryAdapter,
                accountTypeAdapter = AccountTypeAdapter,
            ),
            CustomerSummaryAdapter = CustomerSummary.Adapter(
                balanceAdapter = PaisaAdapter,
                lastActivityAdapter = TimestampAdapter,
                lastAmountAdapter = PaisaAdapter,
                lastPaymentAdapter = TimestampAdapter,
                lastReminderSendTimeAdapter = TimestampAdapter,
            ),
            SupplierSummaryAdapter = SupplierSummary.Adapter(
                balanceAdapter = PaisaAdapter,
                lastActivityAdapter = TimestampAdapter,
                lastAmountAdapter = PaisaAdapter,
            ),
            TransactionCommandAdapter = TransactionCommand.Adapter(
                accountTypeAdapter = AccountTypeAdapter,
                commandTypeAdapter = CommandTypeAdapter,
            ),
        )
    }

    private val customerProjection: CustomerProjection by lazy {
        CustomerProjection(database)
    }

    private val supplierProjection: SupplierProjection by lazy {
        SupplierProjection(database)
    }

    private val transactionProjection: TransactionProjection by lazy {
        TransactionProjection(database)
    }

    fun listAllCustomers(
        businessId: String,
        sortBy: SortBy = SortBy.LAST_ACTIVITY,
        limit: Int = 1000,
        offset: Int = 0,
    ): Flow<List<Customer>> {
        return customerProjection.listAllCustomers(
            businessId = businessId,
            sortBy = sortBy,
            limit = limit,
            offset = offset,
        )
    }

    suspend fun addCustomer(customer: Customer) {
        withContext(appDispatchers.io) { customerProjection.addCustomer(customer) }
    }

    fun getCustomerById(customerId: String): Flow<Customer?> {
        return customerProjection.getCustomerById(customerId)
    }

    fun getSupplierById(supplierId: String): Flow<Supplier?> {
        return supplierProjection.getSupplierById(supplierId)
    }

    fun getCustomerByMobile(mobile: String, businessId: String): Flow<Customer?> {
        return customerProjection.getCustomerByMobile(mobile, businessId)
    }

    fun getSupplierByMobile(mobile: String, businessId: String): Flow<Supplier?> {
        return supplierProjection.getSupplierByMobile(mobile, businessId)
    }

    suspend fun markCustomerAsDeleted(customerId: String) {
        customerProjection.markCustomerAsDeleted(customerId)
    }

    suspend fun addSupplier(supplier: Supplier) {
        withContext(appDispatchers.io) { supplierProjection.addSupplier(supplier) }
    }

    fun listAllSuppliers(
        businessId: String,
        sortBy: SortBy,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return supplierProjection.listAllSuppliers(
            businessId = businessId,
            sortBy = sortBy,
            limit = limit,
            offset = offset,
        )
    }

    suspend fun addTransaction(
        businessId: String,
        command: CreateTransaction,
        transaction: DomainTransaction,
    ) {
        transactionProjection.addTransaction(
            businessId = businessId,
            command = command,
            transaction = transaction,
        )
    }

    suspend fun deleteTransaction(
        command: DeleteTransaction,
        transaction: DomainTransaction,
    ) {
        transactionProjection.deleteTransaction(
            command = command,
            transaction = transaction,
        )
    }

    suspend fun updateTransactionAmount(
        command: UpdateTransactionAmount,
        existingAmount: Paisa,
        businessId: String,
        accountId: String,
        transactionType: DomainTransaction.Type,
    ) {
        transactionProjection.updateTransactionAmount(
            accountId = accountId,
            command = command,
            existingAmount = existingAmount,
            businessId = businessId,
            transactionType = transactionType,
        )
    }

    suspend fun updateTransactionNote(
        businessId: String,
        command: UpdateTransactionNote,
    ) {
        transactionProjection.updateTransactionNote(
            command = command,
            businessId = businessId,
        )
    }

    fun getTransactionDetails(transactionId: String): Flow<DomainTransaction?> {
        return transactionProjection.getTransactionDetails(transactionId)
    }

    fun getTransactionsForAccount(
        accountId: String,
        startTime: Timestamp,
        endTime: Timestamp?,
    ): Flow<List<DomainTransaction>> {
        return transactionProjection.getTransactionsForAccount(accountId, startTime, endTime)
    }

    suspend fun resetCustomerList(customers: List<Customer>, businessId: String) {
        customerProjection.resetCustomerList(customers, businessId)
    }

    suspend fun resetCustomer(customer: Customer) {
        customerProjection.resetCustomer(customer)
    }

    suspend fun getTransactionCommands(
        businessId: String,
        limit: Int,
    ): List<app.okcredit.ledger.core.models.TransactionCommand> {
        return transactionProjection.getTransactionCommands(businessId, limit)
    }

    suspend fun markTransactionsSynced(commands: List<app.okcredit.ledger.core.models.TransactionCommand>) {
        transactionProjection.markTransactionsSynced(commands)
    }

    suspend fun getLastUpdatedTransactionTimestamp(businessId: String): Timestamp {
        return transactionProjection.getLastUpdatedTransactionTimestamp(businessId)
    }

    suspend fun insertTransactions(transactions: List<app.okcredit.ledger.contract.model.Transaction>) {
        transactionProjection.insertTransactions(transactions)
    }

    suspend fun recalculateCustomerSummary(customers: List<String>) {
        customerProjection.recalculateCustomerSummary(customers)
    }

    fun resetSupplierList(suppliers: List<Supplier>, businessId: String) {
        supplierProjection.resetSupplierList(suppliers, businessId)
    }

    suspend fun resetSupplier(supplier: Supplier) {
        supplierProjection.resetSupplier(supplier)
    }

    suspend fun markSupplierAsDeleted(supplierId: String) {
        supplierProjection.markSupplierAsDeleted(supplierId)
    }
}

fun DomainTransaction.toDbTransaction(accountType: AccountType): Transaction {
    return Transaction(
        businessId = this.businessId,
        id = this.id,
        accountId = this.accountId,
        amount = this.amount,
        type = this.type,
        note = this.note,
        billDate = this.billDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt ?: 0.timestamp,
        referenceSource = this.referenceSource,
        state = this.state,
        category = this.category,
        createdByCustomer = this.createdByCustomer,
        deletedByCustomer = this.deletedByCustomer,
        amountUpdated = this.amountUpdated,
        amountUpdatedTime = this.amountUpdateTime,
        referenceId = this.referenceId,
        deleted = this.deleted,
        deleteTime = this.deleteTime,
        dirty = this.dirty,
        accountType = accountType,
    )
}

fun <T : Any, R> Query<T>.mapToDomainList(mapper: (T) -> R): Flow<List<R>> {
    return asFlow()
        .mapToList(appDispatchers.io)
        .map {
            it.map { mapper(it) }
        }
}

fun <T : Any, R> Query<T>.mapToDomain(mapper: (T?) -> R): Flow<R> {
    return asFlow()
        .mapToOneOrNull(appDispatchers.io)
        .map {
            mapper(it)
        }
}

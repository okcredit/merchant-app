package app.okcredit.ledger.core.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.AccountStatus
import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.local.LedgerDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import okcredit.base.appDispatchers

class SupplierProjection(
    private val database: LedgerDatabase,
) {

    private val accountQueries by lazy { database.accountQueries }

    private fun listAllSuppliersByBalance(
        businessId: String,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return accountQueries.allSuppliersByBalanceDue(
            accountType = AccountType.SUPPLIER,
            businessId = businessId,
            limit = limit.toLong(),
            offset = offset.toLong(),
        ).mapToDomainList { supplier ->
            Supplier(
                id = supplier.id,
                businessId = supplier.businessId,
                name = supplier.name,
                profileImage = supplier.profileImage,
                mobile = supplier.mobile,
                createdAt = supplier.createdAt,
                updatedAt = supplier.updatedAt,
                registered = supplier.registered,
                settings = Supplier.SupplierSettings(
                    txnAlertEnabled = supplier.txnAlertEnabled,
                    blockedBySupplier = supplier.blockedBySupplier,
                    addTransactionRestricted = supplier.addTransactionRestricted,
                    lang = supplier.language ?: "en",
                ),
                summary = Supplier.SupplierSummary(
                    balance = supplier.balance,
                    lastActivity = supplier.lastActivity,
                    lastAmount = supplier.lastAmount,
                    lastActivityMetaInfo = supplier.lastActivityMetaInfo.toInt(),
                    transactionCount = supplier.transactionCount,
                ),
                status = supplier.status
            )
        }
    }

    private fun listAllSuppliersByLastActivity(
        businessId: String,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return accountQueries.allSuppliersByLastActivity(
            accountType = AccountType.SUPPLIER,
            businessId = businessId,
            limit = limit.toLong(),
            offset = offset.toLong(),
        ).mapToDomainList { supplier ->
            Supplier(
                id = supplier.id,
                businessId = supplier.businessId,
                name = supplier.name,
                profileImage = supplier.profileImage,
                mobile = supplier.mobile,
                createdAt = supplier.createdAt,
                updatedAt = supplier.updatedAt,
                registered = supplier.registered,
                settings = Supplier.SupplierSettings(
                    txnAlertEnabled = supplier.txnAlertEnabled,
                    blockedBySupplier = supplier.blockedBySupplier,
                    addTransactionRestricted = supplier.addTransactionRestricted,
                    lang = supplier.language ?: "en",
                ),
                summary = Supplier.SupplierSummary(
                    balance = supplier.balance,
                    lastActivity = supplier.lastActivity,
                    lastAmount = supplier.lastAmount,
                    lastActivityMetaInfo = supplier.lastActivityMetaInfo.toInt(),
                    transactionCount = supplier.transactionCount,
                ),
                status = supplier.status
            )
        }
    }

    private fun listAllSuppliersByName(
        businessId: String,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return accountQueries.allSuppliersByName(
            accountType = AccountType.SUPPLIER,
            businessId = businessId,
            limit = limit.toLong(),
            offset = offset.toLong(),
        ).mapToDomainList { supplier ->
            Supplier(
                id = supplier.id,
                businessId = supplier.businessId,
                name = supplier.name,
                profileImage = supplier.profileImage,
                mobile = supplier.mobile,
                createdAt = supplier.createdAt,
                updatedAt = supplier.updatedAt,
                registered = supplier.registered,
                settings = Supplier.SupplierSettings(
                    txnAlertEnabled = supplier.txnAlertEnabled,
                    blockedBySupplier = supplier.blockedBySupplier,
                    addTransactionRestricted = supplier.addTransactionRestricted,
                    lang = supplier.language ?: "en",
                ),
                summary = Supplier.SupplierSummary(
                    balance = supplier.balance,
                    lastActivity = supplier.lastActivity,
                    lastAmount = supplier.lastAmount,
                    lastActivityMetaInfo = supplier.lastActivityMetaInfo.toInt(),
                    transactionCount = supplier.transactionCount,
                ),
                status = supplier.status
            )
        }
    }

    fun getSupplierById(supplierId: String): Flow<Supplier?> {
        return combine(
            accountQueries.accountById(accountId = supplierId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
            accountQueries.supplierSummaryById(supplierId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
            accountQueries.supplierSettingsById(supplierId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
        ) { account, summary, settings ->
            account?.let {
                Supplier(
                    id = account.id,
                    businessId = account.businessId,
                    name = account.name,
                    profileImage = account.profileImage,
                    mobile = account.mobile,
                    createdAt = account.createdAt,
                    updatedAt = account.updatedAt,
                    registered = account.registered,
                    settings = Supplier.SupplierSettings(
                        txnAlertEnabled = settings?.txnAlertEnabled ?: false,
                        addTransactionRestricted = settings?.addTransactionRestricted ?: false,
                        blockedBySupplier = settings?.blockedBySupplier ?: false,
                        lang = settings?.language ?: "en",
                    ),
                    summary = if (summary != null) {
                        Supplier.SupplierSummary(
                            balance = summary.balance,
                            lastActivity = summary.lastActivity,
                            lastAmount = summary.lastAmount,
                            lastActivityMetaInfo = summary.lastActivityMetaInfo.toInt(),
                            transactionCount = summary.transactionCount,
                        )
                    } else {
                        Supplier.SupplierSummary()
                    },
                    status = account.status
                )
            }
        }
    }

    fun getSupplierByMobile(mobile: String, businessId: String): Flow<Supplier?> {
        return accountQueries.accountIdByMobile(
            mobile,
            businessId = businessId,
            accountType = AccountType.SUPPLIER,
        )
            .asFlow()
            .mapToOneOrNull(appDispatchers.io)
            .flatMapLatest { accountId ->
                if (accountId != null) {
                    getSupplierById(accountId)
                } else {
                    flowOf(null)
                }
            }
    }

    fun addSupplier(supplier: Supplier) {
        database.transaction {
            accountQueries.insertOrReplaceAccount(
                Account(
                    id = supplier.id,
                    businessId = supplier.businessId,
                    type = AccountType.SUPPLIER,
                    name = supplier.name,
                    mobile = supplier.mobile,
                    profileImage = supplier.profileImage,
                    createdAt = supplier.createdAt,
                    updatedAt = supplier.updatedAt,
                    registered = supplier.registered,
                    accountUrl = null,
                    gstNumber = null,
                    status = AccountStatus.ACTIVE,
                ),
            )
            addOrUpdateSupplierSettings(supplierId = supplier.id, settings = supplier.settings)
            addOrUpdateSupplierSummary(supplierId = supplier.id, summary = supplier.summary)
        }
    }

    private fun addOrUpdateSupplierSummary(supplierId: String, summary: Supplier.SupplierSummary) {
        accountQueries.insertOrReplaceSupplierSummary(
            SupplierSummary(
                supplierId = supplierId,
                balance = summary.balance,
                lastActivity = summary.lastActivity,
                lastAmount = summary.lastAmount,
                lastActivityMetaInfo = summary.lastActivityMetaInfo.toLong(),
                transactionCount = summary.transactionCount,
            ),
        )
    }

    private fun addOrUpdateSupplierSettings(
        supplierId: String,
        settings: Supplier.SupplierSettings,
    ) {
        accountQueries.insertOrReplaceSupplierSettings(
            SupplierSettings(
                supplierId = supplierId,
                txnAlertEnabled = settings.txnAlertEnabled,
                blockedBySupplier = settings.blockedBySupplier,
                addTransactionRestricted = settings.addTransactionRestricted,
                language = settings.lang,
            ),
        )
    }

    fun listAllSuppliers(
        businessId: String,
        sortBy: SortBy,
        limit: Int,
        offset: Int,
    ): Flow<List<Supplier>> {
        return when (sortBy) {
            SortBy.NAME -> listAllSuppliersByName(businessId, limit, offset)
                .catch { emit(emptyList()) }

            SortBy.BALANCE_DUE -> listAllSuppliersByBalance(businessId, limit, offset)
                .catch { emit(emptyList()) }

            else -> listAllSuppliersByLastActivity(businessId, limit, offset)
                .catch { emit(emptyList()) }
        }
    }

    fun resetSupplierList(suppliers: List<Supplier>, businessId: String) {
        database.transaction {
            accountQueries.deleteAllAccounts(AccountType.SUPPLIER, businessId)
            suppliers.forEach { supplier ->
                addSupplier(supplier)
            }
        }
    }

    suspend fun resetSupplier(supplier: Supplier) {
        withContext(appDispatchers.io) {
            database.transaction {
                accountQueries.insertOrReplaceAccount(
                    Account(
                        id = supplier.id,
                        businessId = supplier.businessId,
                        type = AccountType.SUPPLIER,
                        name = supplier.name,
                        mobile = supplier.mobile,
                        profileImage = supplier.profileImage,
                        createdAt = supplier.createdAt,
                        updatedAt = supplier.updatedAt,
                        registered = supplier.registered,
                        accountUrl = null,
                        gstNumber = null,
                        status = AccountStatus.ACTIVE,
                    ),
                )
                addOrUpdateSupplierSettings(supplierId = supplier.id, settings = supplier.settings)
                addOrUpdateSupplierSummary(supplierId = supplier.id, summary = supplier.summary)
            }
        }
    }
}

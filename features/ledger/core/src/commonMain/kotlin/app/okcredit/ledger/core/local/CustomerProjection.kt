package app.okcredit.ledger.core.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.CustomerStatus
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.local.LedgerDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import okcredit.base.appDispatchers
import okcredit.base.units.paisa
import okcredit.base.units.timestamp

class CustomerProjection(
    private val database: LedgerDatabase,
) {

    private val accountQueries by lazy { database.accountQueries }
    private val transactionQueries by lazy { database.transactionQueries }

    fun listAllCustomers(
        businessId: String,
        sortBy: SortBy = SortBy.LAST_ACTIVITY,
        limit: Int = 1000,
        offset: Int = 0,
    ): Flow<List<Customer>> {
        println("listAllCustomers called with businessId: $businessId, sortBy: $sortBy, limit: $limit, offset: $offset")
        return when (sortBy) {
            SortBy.NAME -> listAllAccountsByName(
                businessId = businessId,
                limit = limit,
                offset = offset,
            )

            SortBy.LAST_ACTIVITY -> listAllAccountsByLastActivity(
                businessId = businessId,
                limit = limit,
                offset = offset,
            )

            SortBy.LAST_PAYMENT -> listAllAccountsByLastPayment(
                businessId = businessId,
                limit = limit,
                offset = offset,
            )

            SortBy.BALANCE_DUE -> listAllAccountsByBalance(
                businessId = businessId,
                limit = limit,
                offset = offset,
            )
        }
    }

    private fun listAllAccountsByName(
        businessId: String,
        limit: Int,
        offset: Int,
    ) = accountQueries.allAccountsByName(
        businessId = businessId,
        limit = limit.toLong(),
        offset = offset.toLong(),
        accountType = AccountType.CUSTOMER,
    ).mapToDomainList { customer ->
        Customer(
            id = customer.id,
            businessId = customer.businessId,
            name = customer.name,
            profileImage = customer.profileImage,
            accountUrl = customer.accountUrl,
            mobile = customer.mobile,
            createdAt = customer.createdAt,
            updatedAt = customer.updatedAt,
            gstNumber = customer.gstNumber,
            registered = customer.registered,
            status = customer.status,
            settings = Customer.CustomerSettings(
                reminderMode = customer.reminderMode,
                restrictContactSync = customer.restrictContactSync,
                txnAlertEnabled = customer.txnAlertEnabled,
                addTransactionRestricted = customer.addTransactionRestricted,
                blockedByCustomer = customer.blockedByCustomer,
            ),
            summary = Customer.CustomerSummary(
                balance = customer.balance,
                lastActivity = customer.lastActivity,
                lastAmount = customer.lastAmount,
                lastActivityMetaInfo = customer.lastActivityMetaInfo,
                lastPayment = customer.lastPayment,
                lastReminderSendTime = customer.lastReminderSendTime,
                transactionCount = customer.transactionCount,
            ),
        )
    }

    private fun listAllAccountsByLastActivity(
        businessId: String,
        limit: Int,
        offset: Int,
    ) = accountQueries.allAccountsByLastActivity(
        businessId = businessId,
        accountType = AccountType.CUSTOMER,
        limit = limit.toLong(),
        offset = offset.toLong(),
    )
        .mapToDomainList { customer ->
            Customer(
                id = customer.id,
                businessId = customer.businessId,
                status = customer.status,
                name = customer.name,
                profileImage = customer.profileImage,
                accountUrl = customer.accountUrl,
                mobile = customer.mobile,
                createdAt = customer.createdAt,
                updatedAt = customer.updatedAt,
                gstNumber = customer.gstNumber,
                registered = customer.registered,
                settings = Customer.CustomerSettings(
                    reminderMode = customer.reminderMode,
                    restrictContactSync = customer.restrictContactSync,
                    txnAlertEnabled = customer.txnAlertEnabled,
                    addTransactionRestricted = customer.addTransactionRestricted,
                    blockedByCustomer = customer.blockedByCustomer,
                ),
                summary = Customer.CustomerSummary(
                    balance = customer.balance,
                    lastActivity = customer.lastActivity,
                    lastAmount = customer.lastAmount,
                    lastActivityMetaInfo = customer.lastActivityMetaInfo,
                    lastPayment = customer.lastPayment,
                    lastReminderSendTime = customer.lastReminderSendTime,
                    transactionCount = customer.transactionCount,
                ),
            )
        }

    private fun listAllAccountsByBalance(
        businessId: String,
        limit: Int,
        offset: Int,
    ) = accountQueries.allAccountsByBalanceDue(
        businessId = businessId,
        accountType = AccountType.CUSTOMER,
        limit = limit.toLong(),
        offset = offset.toLong(),
    )
        .mapToDomainList { customer ->
            Customer(
                id = customer.id,
                businessId = customer.businessId,
                status = customer.status,
                name = customer.name,
                profileImage = customer.profileImage,
                accountUrl = customer.accountUrl,
                mobile = customer.mobile,
                createdAt = customer.createdAt,
                updatedAt = customer.updatedAt,
                gstNumber = customer.gstNumber,
                registered = customer.registered,
                settings = Customer.CustomerSettings(
                    reminderMode = customer.reminderMode,
                    restrictContactSync = customer.restrictContactSync,
                    txnAlertEnabled = customer.txnAlertEnabled,
                    addTransactionRestricted = customer.addTransactionRestricted,
                    blockedByCustomer = customer.blockedByCustomer,
                ),
                summary = Customer.CustomerSummary(
                    balance = customer.balance,
                    lastActivity = customer.lastActivity,
                    lastAmount = customer.lastAmount,
                    lastActivityMetaInfo = customer.lastActivityMetaInfo,
                    lastPayment = customer.lastPayment,
                    lastReminderSendTime = customer.lastReminderSendTime,
                    transactionCount = customer.transactionCount,
                ),
            )
        }

    private fun listAllAccountsByLastPayment(
        businessId: String,
        limit: Int,
        offset: Int,
    ) = accountQueries.allAccountsByLastPayment(
        businessId = businessId,
        accountType = AccountType.CUSTOMER,
        limit = limit.toLong(),
        offset = offset.toLong(),
    )
        .mapToDomainList { customer ->
            Customer(
                id = customer.id,
                businessId = customer.businessId,
                status = customer.status,
                name = customer.name,
                profileImage = customer.profileImage,
                accountUrl = customer.accountUrl,
                mobile = customer.mobile,
                createdAt = customer.createdAt,
                updatedAt = customer.updatedAt,
                gstNumber = customer.gstNumber,
                registered = customer.registered,
                settings = Customer.CustomerSettings(
                    reminderMode = customer.reminderMode,
                    restrictContactSync = customer.restrictContactSync,
                    txnAlertEnabled = customer.txnAlertEnabled,
                    addTransactionRestricted = customer.addTransactionRestricted,
                    blockedByCustomer = customer.blockedByCustomer,
                ),
                summary = Customer.CustomerSummary(
                    balance = customer.balance,
                    lastActivity = customer.lastActivity,
                    lastAmount = customer.lastAmount,
                    lastActivityMetaInfo = customer.lastActivityMetaInfo,
                    lastPayment = customer.lastPayment,
                    lastReminderSendTime = customer.lastReminderSendTime,
                    transactionCount = customer.transactionCount,
                ),
            )
        }

    suspend fun resetCustomerList(customers: List<Customer>, businessId: String) {
        withContext(appDispatchers.io) {
            database.transaction {
                accountQueries.deleteAllCustomers(AccountType.CUSTOMER, businessId)
                customers.forEach { customer ->
                    accountQueries.insertOrReplaceAccount(
                        Account(
                            id = customer.id,
                            businessId = customer.businessId,
                            type = AccountType.CUSTOMER,
                            status = customer.status,
                            name = customer.name,
                            mobile = customer.mobile,
                            profileImage = customer.profileImage,
                            accountUrl = customer.accountUrl,
                            createdAt = customer.createdAt,
                            updatedAt = customer.updatedAt,
                            gstNumber = customer.gstNumber,
                            registered = customer.registered,
                        ),
                    )
                    addOrUpdateCustomerSettings(customer.id, customer.settings)
                    addCustomerSummaryOrIgnore(customer.id, customer.summary)
                }
            }
        }
    }

    suspend fun addCustomer(customer: Customer) {
        withContext(appDispatchers.io) {
            database.transaction {
                accountQueries.insertOrReplaceAccount(
                    Account = Account(
                        id = customer.id,
                        businessId = customer.businessId,
                        type = AccountType.CUSTOMER,
                        status = customer.status,
                        name = customer.name,
                        mobile = customer.mobile,
                        profileImage = customer.profileImage,
                        accountUrl = customer.accountUrl,
                        createdAt = customer.createdAt,
                        updatedAt = customer.updatedAt,
                        gstNumber = customer.gstNumber,
                        registered = customer.registered,
                    ),
                )
                addOrUpdateCustomerSettings(customer.id, customer.settings)
                addCustomerSummaryOrIgnore(customer.id, customer.summary)
            }
        }
    }

    private fun addCustomerSummaryOrIgnore(customerId: String, summary: Customer.CustomerSummary) {
        accountQueries.insertOrIgnoreCustomerSummary(
            CustomerSummary = CustomerSummary(
                customerId = customerId,
                balance = summary.balance,
                lastActivityMetaInfo = summary.lastActivityMetaInfo,
                lastActivity = summary.lastActivity,
                lastAmount = summary.lastAmount,
                lastPayment = summary.lastPayment,
                lastReminderSendTime = summary.lastReminderSendTime,
                transactionCount = summary.transactionCount,
            ),
        )
    }

    private fun addOrUpdateCustomerSettings(
        customerId: String,
        settings: Customer.CustomerSettings,
    ) {
        accountQueries.insertOrReplaceCustomerSettings(
            CustomerSettings(
                customerId = customerId,
                reminderMode = settings.reminderMode,
                restrictContactSync = settings.restrictContactSync,
                addTransactionRestricted = settings.addTransactionRestricted,
                blockedByCustomer = settings.blockedByCustomer,
                txnAlertEnabled = settings.txnAlertEnabled,
                language = settings.language,
            ),
        )
    }

    fun getCustomerById(customerId: String): Flow<Customer?> {
        return combine(
            accountQueries.accountById(accountId = customerId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
            accountQueries.customerSummaryById(customerId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
            accountQueries.customerSettingsById(customerId)
                .asFlow()
                .mapToOneOrNull(appDispatchers.io),
        ) { account, customerSummary, customerSettings ->
            account?.let {
                Customer(
                    id = account.id,
                    businessId = account.businessId,
                    status = account.status,
                    name = account.name,
                    profileImage = account.profileImage,
                    accountUrl = account.accountUrl,
                    mobile = account.mobile,
                    createdAt = account.createdAt,
                    updatedAt = account.updatedAt,
                    gstNumber = account.gstNumber,
                    registered = account.registered,
                    settings = Customer.CustomerSettings(
                        reminderMode = customerSettings?.reminderMode,
                        restrictContactSync = customerSettings?.restrictContactSync ?: false,
                        txnAlertEnabled = customerSettings?.txnAlertEnabled ?: false,
                        addTransactionRestricted = customerSettings?.addTransactionRestricted
                            ?: false,
                        blockedByCustomer = customerSettings?.blockedByCustomer ?: false,
                        language = customerSettings?.language,
                    ),
                    summary = if (customerSummary != null) {
                        Customer.CustomerSummary(
                            balance = customerSummary.balance,
                            lastActivity = customerSummary.lastActivity,
                            lastAmount = customerSummary.lastAmount,
                            lastActivityMetaInfo = customerSummary.lastActivityMetaInfo,
                            lastPayment = customerSummary.lastPayment,
                            lastReminderSendTime = customerSummary.lastReminderSendTime,
                            transactionCount = customerSummary.transactionCount,
                        )
                    } else {
                        Customer.CustomerSummary()
                    },
                )
            }
        }
    }

    fun getCustomerByMobile(mobile: String, businessId: String): Flow<Customer?> {
        return accountQueries.accountIdByMobile(
            mobile = mobile,
            businessId = businessId,
            accountType = AccountType.CUSTOMER,
        )
            .asFlow()
            .mapToOneOrNull(appDispatchers.io)
            .flatMapLatest { accountId ->
                if (accountId != null) {
                    getCustomerById(accountId)
                } else {
                    flowOf(null)
                }
            }
    }

    suspend fun markCustomerAsDeleted(customerId: String) {
        withContext(appDispatchers.io) {
            accountQueries.updateAccountStatus(
                status = CustomerStatus.DELETED,
                accountId = customerId,
            )
        }
    }

    suspend fun recalculateCustomerSummary(customers: List<String>) {
        withContext(appDispatchers.io) {
            database.transaction {
                customers.forEach { customer ->
                    val summary = transactionQueries.summaryForCustomer(accountId = customer)
                        .executeAsOneOrNull()
                    summary?.let {
                        accountQueries.updateCustomerSummary(
                            customerId = customer,
                            balance = summary.balance?.paisa ?: 0.paisa,
                            lastActivity = summary.lastActivity ?: 0.timestamp,
                            lastAmount = summary.lastAmount,
                            lastActivityMetaInfo = summary.lastActivityMetaInfo ?: 4,
                            lastPayment = summary.lastPayment?.timestamp,
                            transactionCount = summary.transactionCount ?: 0L,
                        )
                    }
                }
            }
        }
    }
}

package okcredit.ledger.core

import app.cash.sqldelight.logs.LogSqliteDriver
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import app.okcredit.ledger.core.usecase.RecordTransaction
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.datetime.Clock
import okcredit.base.randomUUID
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import okcredit.ledger.core.di.provideDriver
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

class LedgerTestHelper {

    val getActiveBusinessId: GetActiveBusinessId by lazy {
        mock<GetActiveBusinessId> {
            everySuspend { execute() } returns "business-id"
        }
    }

    val apiClient = mock<LedgerApiClient>()

    val ledgerLocalSource: LedgerLocalSource by lazy {
        LedgerLocalSource(
            lazy {
                LogSqliteDriver(sqlDriver = provideDriver()) {
                    println(it)
                }
            },
        )
    }

    val remoteSource = LedgerRemoteSource(lazy { apiClient })

    val customerRepository: CustomerRepository by lazy {
        CustomerRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(remoteSource),
        )
    }

    val supplierRepository: SupplierRepository by lazy {
        SupplierRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(remoteSource),
        )
    }

    val recordTransaction = RecordTransaction(
        getActiveBusinessIdLazy = lazyOf(getActiveBusinessId),
        ledgerLocalSourceLazy = lazyOf(ledgerLocalSource),
        ledgerSyncManagerLazy = lazyOf(
            LedgerSyncManager(
                transactionSyncer = mock(MockMode.autoUnit),
                customerSyncer = mock(MockMode.autoUnit),
                supplierSyncer = mock(MockMode.autoUnit),
            ),
        ),
    )

    val ledgerSyncManager = LedgerSyncManager(
        transactionSyncer = mock(MockMode.autoUnit),
        customerSyncer = mock(MockMode.autoUnit),
        supplierSyncer = mock(MockMode.autoUnit),
    )

    val successResponse = mock<HttpResponse> {
        every { status } returns HttpStatusCode.OK
    }

    suspend fun addSomeTransaction(relationId: String): Transaction {
        return recordTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = (100..1000).random().times(100).paisa,
            type = Transaction.Type.CREDIT,
            billDate = Clock.System.now().timestamp,
            note = "note ${randomUUID()}",
            bills = emptyList(),
        )
    }

    suspend fun addSomeCustomer(businessId: String): String {
        everySuspend {
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(
            body = someApiCustomer("customer", "1234567890", null),
            rawResponse = mock<HttpResponse> {
                every { status } returns HttpStatusCode.OK
            },
        )
        return customerRepository.addCustomer(
            businessId = businessId,
            name = "customer",
            mobile = "1234567890",
            reactivate = false,
        ).id
    }

    fun addSomeBusiness(): String {
        everySuspend { getActiveBusinessId.execute() } returns "business-id"
        return "business-id"
    }

    fun someApiCustomer(name: String, mobile: String, profileImage: String? = null): ApiCustomer {
        return ApiCustomer(
            id = randomUUID(),
            userId = randomUUID(),
            description = name,
            mobile = mobile,
            profileImage = profileImage,
            createdAt = 0L,
            registered = false,
            state = 1,
            gstNumber = null,
            email = null,
            accountUrl = null,
            address = null,
            txnStartTime = null,
            txnAlertEnabled = false,
            lang = null,
            reminderMode = null,
            dueCustomDate = null,
            dueReminderEnabledSet = null,
            dueCreditPeriodSet = null,
            isLiveSales = false,
            displayTxnAlertSetting = null,
            addTransactionRestricted = false,
            blockedByCustomer = false,
            restrictContactSync = false,
            lastReminderSent = null,
            updatedAt = 0L,
            status = 1,
        )
    }

    fun someApiSupplier(name: String, mobile: String, profileImage: String? = null): ApiSupplier {
        return ApiSupplier(
            id = randomUUID(),
            name = name,
            mobile = mobile,
            profileImage = profileImage,
            createTime = 0L,
            registered = false,
            state = 1,
            address = null,
            txnStartTime = null,
            txnAlertEnabled = false,
            lang = null,
            displayTxnAlertSetting = null,
            addTransactionRestricted = false,
            blockedBySupplier = false,
            restrictContactSync = false,
            status = 1,
        )
    }

}
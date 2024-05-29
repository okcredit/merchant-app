package okcredit.ledger.core

import app.cash.sqldelight.logs.LogSqliteDriver
import app.okcredit.ledger.contract.Ledger
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.LedgerImpl
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import app.okcredit.ledger.core.usecase.AddAccount
import app.okcredit.ledger.core.usecase.DeleteCustomer
import app.okcredit.ledger.core.usecase.RecordTransaction
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import okcredit.base.randomUUID
import okcredit.ledger.core.di.provideDriver
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import kotlin.test.BeforeTest

class LedgerTest {

    private lateinit var ledger: Ledger

    private val apiClient = mock<LedgerApiClient>()

    private val getActiveBusinessId: GetActiveBusinessId by lazy {
        mock<GetActiveBusinessId> {
            everySuspend { execute() } returns "business-id"
        }
    }

    private val ledgerLocalSource: LedgerLocalSource by lazy {
        LedgerLocalSource(
            lazy {
                LogSqliteDriver(sqlDriver = provideDriver()) {
                    println(it)
                }
            },
        )
    }

    private val remoteSource = LedgerRemoteSource(lazy { apiClient })

    private val recordTransaction: RecordTransaction by lazy {
        RecordTransaction(
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
    }

    private val customerRepository: CustomerRepository by lazy {
        CustomerRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(remoteSource),
        )
    }

    private val supplierRepository: SupplierRepository by lazy {
        SupplierRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(remoteSource),
        )
    }

    private val dummyHttpResponse = mock<HttpResponse> {
        every { status } returns HttpStatusCode.OK
    }

    @BeforeTest
    fun setup() {
        ledger = LedgerImpl(
            recordTransactionLazy = lazyOf(recordTransaction),
            addAccountLazy = lazyOf(
                AddAccount(
                    getActiveBusinessIdLazy = lazyOf(getActiveBusinessId),
                    customerRepositoryLazy = lazyOf(customerRepository),
                    supplierRepositoryLazy = lazyOf(supplierRepository),
                ),
            ),
            deleteCustomerLazy = lazyOf(
                DeleteCustomer(
                    repository = customerRepository,
                ),
            ),
        )
    }
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

package okcredit.ledger.core.usecase

import app.cash.sqldelight.logs.LogSqliteDriver
import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.usecase.GetTransactionsForAccount
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import app.okcredit.ledger.core.usecase.GetCustomerTransactionsImpl
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import okcredit.base.randomUUID
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import okcredit.ledger.core.di.provideDriver
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCustomerTransactionsTest {

    private lateinit var getTransactionsForAccount: GetTransactionsForAccount

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

    private val remoteSource = LedgerRemoteSource(lazyOf(apiClient))

    private val customerRepository: CustomerRepository by lazy {
        CustomerRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(remoteSource),
        )
    }

    private val recordTransaction = RecordTransaction(
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

    @BeforeTest
    fun setup() {
        getTransactionsForAccount = GetCustomerTransactionsImpl(
            repository = customerRepository,
        )
    }

    @Test
    fun `all added customer transactions should be returned`() = runTest {
        val businessId = addSomeBusiness()

        val relationId = addSomeRelation(businessId)

        addSomeTransaction(relationId)

        addSomeTransaction(relationId)

        val transactions = getTransactionsForAccount.execute(relationId, AccountType.CUSTOMER).firstOrNull()
        assertEquals(2, transactions?.size)
    }

    @Test
    fun `new transactions added are emitted from the Flow`() = runTest {
        val businessId = addSomeBusiness()

        val relationId = addSomeRelation(businessId)

        addSomeTransaction(relationId)

        addSomeTransaction(relationId)

        getTransactionsForAccount.execute(relationId, AccountType.CUSTOMER).test {
            assertEquals(awaitItem().size, 2)
            addSomeTransaction(relationId)
            assertEquals(awaitItem().size, 3)
            addSomeTransaction(relationId)
            assertEquals(awaitItem().size, 4)
        }
    }

    private suspend fun addSomeTransaction(relationId: String): Transaction {
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

    private suspend fun addSomeRelation(businessId: String): String {
        everySuspend {
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(
            body = someCustomer("customer", "1234567890", null),
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

    private suspend fun addSomeBusiness(): String {
        everySuspend { getActiveBusinessId.execute() } returns "business-id"
        return "business-id"
    }
}

package okcredit.ledger.core.usecase

import app.cash.sqldelight.logs.LogSqliteDriver
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.usecase.InvalidAmountError
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
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
import kotlinx.coroutines.flow.first
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
import kotlin.test.assertFailsWith

class AddCustomerTransactionTest {

    private lateinit var addCustomerTransaction: RecordTransaction

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

    private val customerRepository: CustomerRepository by lazy {
        CustomerRepository(
            localSourceLazy = lazyOf(ledgerLocalSource),
            remoteSourceLazy = lazyOf(LedgerRemoteSource(lazyOf(apiClient))),
        )
    }

    private val dummyHttpResponse = mock<HttpResponse> {
        every { status } returns HttpStatusCode.OK
    }

    @BeforeTest
    fun setup() {
        addCustomerTransaction = RecordTransaction(
            getActiveBusinessIdLazy = lazyOf(getActiveBusinessId),
            ledgerLocalSourceLazy = lazyOf(ledgerLocalSource),
            ledgerSyncManagerLazy = lazyOf(
                LedgerSyncManager(
                    transactionSyncer = mock(MockMode.autoUnit),
                    customerSyncer = mock(MockMode.autoUnit),
                    supplierSyncer = mock(MockMode.autoUnit),
                )
            )
        )
    }

    @Test
    fun `addCustomerTransaction fails if amount is zero`() = runTest {
        val businessId = addSomeBusiness()
        val relationId = addSomeRelation(businessId)

        assertFailsWith<InvalidAmountError> {
            addCustomerTransaction.execute(
                transactionId = randomUUID(),
                accountId = relationId,
                accountType = AccountType.CUSTOMER,
                amount = 0.paisa,
                type = Transaction.Type.CREDIT,
                billDate = Clock.System.now().timestamp,
                bills = emptyList(),
                note = "note",
            )
        }
    }

    @Test
    fun `add payment adds a transaction to the ledger`() = runTest {
        val businessId = addSomeBusiness()
        val relationId = addSomeRelation(businessId)

        val transaction = addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.PAYMENT,
            billDate = Clock.System.now().timestamp,
        )

        val dbTransaction = customerRepository.getTransactionDetails(transaction.id).firstOrNull()
        assertEquals(1000.paisa, dbTransaction?.amount)
        assertEquals(Transaction.Type.PAYMENT, dbTransaction?.type)

        val account = customerRepository.getCustomerDetails(relationId).firstOrNull()
        assertEquals(1000.paisa, account?.summary?.balance)
    }

    @Test
    fun `record credit adds a transaction to the ledger and update balance`() = runTest {
        val businessId = addSomeBusiness()
        val accountId = addSomeRelation(businessId)

        val transaction = addCustomerTransaction.execute(
            transactionId = businessId,
            accountId = accountId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.CREDIT,
            billDate = Clock.System.now().timestamp,
        )

        val dbTransaction = customerRepository.getTransactionDetails(transaction.id).firstOrNull()
        assertEquals(1000.paisa, dbTransaction?.amount)
        assertEquals(Transaction.Type.CREDIT, dbTransaction?.type)

        val account = customerRepository.getCustomerDetails(accountId).first()
        assertEquals((-1000).paisa, account?.summary?.balance)
    }

    @Test
    fun `add customer transaction updates the balance on multiple transactions`() = runTest {
        val businessId = addSomeBusiness()
        val relationId = addSomeRelation(businessId)

        val payment = addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.PAYMENT,
            billDate = Clock.System.now().timestamp,
        )
        val account = customerRepository.getCustomerDetails(relationId).firstOrNull()

        assertEquals(1000.paisa, account?.summary?.balance)
        assertEquals(payment.createdAt, account?.summary?.lastPayment)
        assertEquals(1000.paisa, account?.summary?.lastAmount)
        assertEquals(3, account?.summary?.lastActivityMetaInfo)

        addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 2000.paisa,
            type = Transaction.Type.CREDIT,
            billDate = Clock.System.now().timestamp,
        )
        val account2 = customerRepository.getCustomerDetails(relationId).firstOrNull()
        println(account2)
        assertEquals((-1000).paisa, account2?.summary?.balance)
        assertEquals(payment.createdAt, account2?.summary?.lastPayment)
        assertEquals(2000.paisa, account2?.summary?.lastAmount)
        assertEquals(2, account2?.summary?.lastActivityMetaInfo)


        val secondPayment = addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 3000.paisa,
            type = Transaction.Type.PAYMENT,
            billDate = Clock.System.now().timestamp,
        )
        val account3 = customerRepository.getCustomerDetails(relationId).firstOrNull()
        assertEquals((2000).paisa, account3?.summary?.balance)
        assertEquals(secondPayment.createdAt, account3?.summary?.lastPayment)
        assertEquals(3000.paisa, account3?.summary?.lastAmount)
        assertEquals(3, account?.summary?.lastActivityMetaInfo)

        val transactions = customerRepository.getTransactionsForCustomer(relationId).firstOrNull()
        assertEquals(3, transactions?.size)
    }

    private suspend fun addSomeRelation(businessId: String): String {
        everySuspend {
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(someCustomer("customer", "1234567890", null), dummyHttpResponse)
        return customerRepository.addCustomer(
            businessId = businessId,
            name = "customer",
            mobile = "1234567890",
            reactivate = false,
        ).id
    }

    private fun addSomeBusiness(): String {
        everySuspend { getActiveBusinessId.execute() } returns "business-id"
        return "business-id"
    }
}

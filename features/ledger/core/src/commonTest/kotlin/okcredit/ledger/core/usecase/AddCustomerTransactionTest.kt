package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.usecase.InvalidAmountError
import app.okcredit.ledger.core.usecase.RecordTransaction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import okcredit.base.randomUUID
import okcredit.base.units.paisa
import okcredit.base.units.timestamp
import okcredit.ledger.core.LedgerTestHelper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddCustomerTransactionTest {

    private lateinit var addCustomerTransaction: RecordTransaction

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        addCustomerTransaction = RecordTransaction(
            getActiveBusinessIdLazy = lazyOf(ledgerTestHelper.getActiveBusinessId),
            ledgerLocalSourceLazy = lazyOf(ledgerTestHelper.ledgerLocalSource),
            ledgerSyncManagerLazy = lazyOf(ledgerTestHelper.ledgerSyncManager),
        )
    }

    @Test
    fun `addCustomerTransaction fails if amount is zero`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

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
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.PAYMENT,
            billDate = Clock.System.now().timestamp,
        )

        val dbTransaction = ledgerTestHelper.ledgerLocalSource.getTransactionDetails(transaction.id).firstOrNull()
        assertEquals(1000.paisa, dbTransaction?.amount)
        assertEquals(Transaction.Type.PAYMENT, dbTransaction?.type)

        val account = ledgerTestHelper.customerRepository.getCustomerDetails(relationId).firstOrNull()
        assertEquals(1000.paisa, account?.summary?.balance)
    }

    @Test
    fun `record credit adds a transaction to the ledger and update balance`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val accountId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = addCustomerTransaction.execute(
            transactionId = businessId,
            accountId = accountId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.CREDIT,
            billDate = Clock.System.now().timestamp,
        )

        val dbTransaction = ledgerTestHelper.ledgerLocalSource.getTransactionDetails(transaction.id).firstOrNull()
        assertEquals(1000.paisa, dbTransaction?.amount)
        assertEquals(Transaction.Type.CREDIT, dbTransaction?.type)

        val account = ledgerTestHelper.customerRepository.getCustomerDetails(accountId).first()
        assertEquals((-1000).paisa, account?.summary?.balance)
    }

    @Test
    fun `add customer transaction updates the balance on multiple transactions`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

        val payment = addCustomerTransaction.execute(
            transactionId = randomUUID(),
            accountId = relationId,
            accountType = AccountType.CUSTOMER,
            amount = 1000.paisa,
            type = Transaction.Type.PAYMENT,
            billDate = Clock.System.now().timestamp,
        )
        val account = ledgerTestHelper.customerRepository.getCustomerDetails(relationId).firstOrNull()

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
        val account2 = ledgerTestHelper.customerRepository.getCustomerDetails(relationId).firstOrNull()
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
        val account3 = ledgerTestHelper.customerRepository.getCustomerDetails(relationId).firstOrNull()
        assertEquals((2000).paisa, account3?.summary?.balance)
        assertEquals(secondPayment.createdAt, account3?.summary?.lastPayment)
        assertEquals(3000.paisa, account3?.summary?.lastAmount)
        assertEquals(3, account?.summary?.lastActivityMetaInfo)

        ledgerTestHelper.ledgerLocalSource.getTransactionsForAccount(
            accountId = relationId,
            startTime = 0.timestamp,
            endTime = Clock.System.now().timestamp,
        ).test {
            assertEquals(3, awaitItem().size)
        }
    }
}

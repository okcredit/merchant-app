package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.usecase.GetAccountStatement
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import kotlinx.coroutines.test.runTest
import okcredit.ledger.core.LedgerTestHelper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetCustomerTransactionsTest {

    private lateinit var getAccountStatement: GetAccountStatement

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        getAccountStatement = GetAccountStatementImpl(
            localSource = ledgerTestHelper.ledgerLocalSource,
        )
    }

    @Test
    fun `all added customer transactions should be returned`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()

        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

        ledgerTestHelper.addSomeTransaction(relationId)

        ledgerTestHelper.addSomeTransaction(relationId)

        getAccountStatement.execute(relationId).test {
            val transactions = awaitItem()
            assertEquals(2, transactions.size)
        }
    }

    @Test
    fun `new transactions added are emitted from the Flow`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()

        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

        ledgerTestHelper.addSomeTransaction(relationId)

        ledgerTestHelper.addSomeTransaction(relationId)

        getAccountStatement.execute(relationId).test {
            assertEquals(2, awaitItem().size)
            ledgerTestHelper.addSomeTransaction(relationId)
            assertEquals(3, awaitItem().size)
            ledgerTestHelper.addSomeTransaction(relationId)
            assertEquals(4, awaitItem().size)
        }
    }
}

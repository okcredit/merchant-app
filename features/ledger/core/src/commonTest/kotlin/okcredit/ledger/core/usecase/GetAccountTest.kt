package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.usecase.GetAccount
import app.okcredit.ledger.core.usecase.GetAccountImpl
import kotlinx.coroutines.test.runTest
import okcredit.ledger.core.LedgerTestHelper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAccountTest {

    private lateinit var getAccount: GetAccount

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        getAccount = GetAccountImpl(
            customerRepository = ledgerTestHelper.customerRepository,
            supplierRepository = ledgerTestHelper.supplierRepository,
        )
    }

    @Test
    fun `added customer or supplier is returned when account id is passed`() = runTest {
        // Given
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)

        // When
        getAccount.execute(relationId, AccountType.CUSTOMER).test {
            // Then
            assertEquals(relationId, awaitItem()?.id)
        }
    }
}

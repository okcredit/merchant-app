package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.model.CustomerStatus
import app.okcredit.ledger.contract.usecase.BalanceNonZeroError
import app.okcredit.ledger.contract.usecase.CustomerNotFoundError
import app.okcredit.ledger.core.usecase.DeleteCustomer
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import kotlinx.coroutines.test.runTest
import okcredit.ledger.core.LedgerTestHelper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteCustomerTest {

    private lateinit var deleteCustomer: DeleteCustomer

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        deleteCustomer = DeleteCustomer(
            repository = ledgerTestHelper.customerRepository,
        )
    }

    @Test
    fun `if the customer does not exist then throw CustomerNotFoundError`() = runTest {
        // Given
        val customerId = "customer-id"

        // When
        assertFailsWith<CustomerNotFoundError> {
            deleteCustomer.execute(customerId)
        }
    }

    @Test
    fun `if customer exist with zero balance then update the customer status to DELETED`() =
        runTest {
            val businessId = ledgerTestHelper.addSomeBusiness()
            val relationId = ledgerTestHelper.addSomeCustomer(businessId)

            everySuspend {
                ledgerTestHelper.apiClient.deleteCustomer(
                    customerId = any(),
                    businessId = any(),
                )
            } returns Response.success(Unit, ledgerTestHelper.successResponse)

            // When
            deleteCustomer.execute(relationId)

            // Then
            ledgerTestHelper.customerRepository.getCustomerDetails(relationId).test {
                assertEquals(awaitItem()?.status, CustomerStatus.DELETED)
            }
        }

    @Test
    fun `if customer exist with non-zero balance then throw BalanceNonZeroError`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)
        ledgerTestHelper.addSomeTransaction(relationId)

        everySuspend {
            ledgerTestHelper.apiClient.deleteCustomer(
                customerId = any(),
                businessId = any(),
            )
        } returns Response.success(Unit, ledgerTestHelper.successResponse)

        // When
        assertFailsWith<BalanceNonZeroError> {
            deleteCustomer.execute(relationId)
        }
    }
}

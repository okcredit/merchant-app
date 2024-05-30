package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.usecase.CyclicAccountError
import app.okcredit.ledger.contract.usecase.InvalidNameError
import app.okcredit.ledger.contract.usecase.MobileConflictError
import app.okcredit.ledger.core.usecase.AddAccount
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

class AddCustomerTest {

    private lateinit var addCustomer: AddAccount

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        addCustomer = AddAccount(
            getActiveBusinessIdLazy = lazyOf(ledgerTestHelper.getActiveBusinessId),
            customerRepositoryLazy = lazyOf(ledgerTestHelper.customerRepository),
            supplierRepositoryLazy = lazyOf(ledgerTestHelper.supplierRepository),
        )
    }

    @Test
    fun `add customer with empty name throws error`() = runTest {
        // Given
        val name = ""
        val mobile = "9876543210"

        // When
        assertFailsWith<InvalidNameError> {
            addCustomer.execute(
                name = name,
                mobile = mobile,
                accountType = AccountType.CUSTOMER,
            )
        }
    }

    @Test
    fun `add customer with valid params adds the customer`() = runTest {
        // Given
        val name = "John Doe"
        val mobile = "9876543210"
        val profileImage = "https://example.com/image.jpg"

        everySuspend {
            ledgerTestHelper.apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(ledgerTestHelper.someApiCustomer(name, mobile, profileImage), ledgerTestHelper.successResponse)

        // When
        val customer = addCustomer.execute(
            name = name,
            mobile = mobile,
            accountType = AccountType.CUSTOMER,
        )

        // Then
        ledgerTestHelper.ledgerLocalSource.getCustomerById(customer.id).test {
            val item = awaitItem()
            assertEquals(customer.id, item?.id)
            assertEquals(customer.name, item?.name)
            assertEquals(customer.mobile, item?.mobile)
            assertEquals(customer.profileImage, item?.profileImage)
        }
    }

    @Test
    fun `if customer already exist with same mobile then throw Mobile conflict error`() = runTest {
        // Given
        val mock = everySuspend {
            ledgerTestHelper.apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        }

        // add first customer
        mock.returns(
            Response.success(
                ledgerTestHelper.someApiCustomer("John Doe", "9876543210"),
                ledgerTestHelper.successResponse
            )
        )
        val firstCustomer = addCustomer.execute(
            name = "John Doe",
            mobile = "9876543210",
            accountType = AccountType.CUSTOMER,
        )

        // add second customer
        val error = assertFailsWith<MobileConflictError> {
            addCustomer.execute(
                name = "John Doe",
                mobile = "9876543210",
                accountType = AccountType.CUSTOMER,
            )
        }

        assertEquals(firstCustomer.id, error.accountId)
    }

    @Test
    fun `if supplier already exist with same mobile then throw cyclic account error`() = runTest {
        // Given
        val mock = everySuspend {
            ledgerTestHelper.apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        }

        // add first customer
        mock.returns(
            value = Response.success(
                body = ledgerTestHelper.someApiSupplier(
                    name = "John Doe",
                    mobile = "9876543210",
                ),
                rawResponse = ledgerTestHelper.successResponse,
            ),
        )
        val supplier = ledgerTestHelper.supplierRepository.addSupplier(
            businessId = "business-id",
            name = "John Doe",
            mobile = "9876543210",
        )

        val error = assertFailsWith<CyclicAccountError> {
            addCustomer.execute(
                name = "John Doe",
                mobile = "9876543210",
                accountType = AccountType.CUSTOMER,
            )
        }

        assertEquals(supplier.id, error.accountId)
    }
}

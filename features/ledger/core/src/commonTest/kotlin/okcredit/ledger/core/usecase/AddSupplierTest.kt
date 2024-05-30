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

class AddSupplierTest {

    private lateinit var addSupplier: AddAccount

    private val ledgerTestHelper = LedgerTestHelper()


    @BeforeTest
    fun setup() {
        addSupplier = AddAccount(
            getActiveBusinessIdLazy = lazyOf(ledgerTestHelper.getActiveBusinessId),
            customerRepositoryLazy = lazyOf(ledgerTestHelper.customerRepository),
            supplierRepositoryLazy = lazyOf(ledgerTestHelper.supplierRepository),
        )
    }

    @Test
    fun `add supplier with empty name throws error`() = runTest {
        // Given
        val name = ""
        val mobile = "9876543210"
        // When
        assertFailsWith<InvalidNameError> {
            addSupplier.execute(
                name = name,
                mobile = mobile,
                accountType = AccountType.SUPPLIER,
            )
        }
    }

    @Test
    fun `add supplier with valid params adds the supplier`() = runTest {
        // Given
        val name = "John Doe"
        val mobile = "9876543210"
        everySuspend {
            ledgerTestHelper.apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(ledgerTestHelper.someApiSupplier(name, mobile), ledgerTestHelper.successResponse)

        // When
        val supplier = addSupplier.execute(
            name = name,
            mobile = mobile,
            accountType = AccountType.SUPPLIER,
        )

        // Then
        ledgerTestHelper.supplierRepository.getSupplierDetails(supplier.id).test {
            val item = awaitItem()
            assertEquals(supplier.id, item?.id)
            assertEquals(supplier.name, item?.name)
            assertEquals(supplier.mobile, item?.mobile)
            assertEquals(supplier.profileImage, item?.profileImage)
        }
    }

    @Test
    fun `if supplier already exist with same mobile then throw Mobile conflict error`() = runTest {
        // Given
        val mock = everySuspend {
            ledgerTestHelper.apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        }

        // add first supplier
        mock.returns(Response.success(ledgerTestHelper.someApiSupplier("John Doe", "9876543210"), ledgerTestHelper.successResponse))
        val firstCustomer = addSupplier.execute(
            name = "John Doe",
            mobile = "9876543210",
            accountType = AccountType.SUPPLIER,
        )

        // add second supplier
        val error = assertFailsWith<MobileConflictError> {
            addSupplier.execute(
                name = "John Doe",
                mobile = "9876543210",
                accountType = AccountType.SUPPLIER,
            )
        }

        assertEquals(firstCustomer.id, error.accountId)
    }

    @Test
    fun `if customer already exist with same mobile then throw cyclic account error`() = runTest {
        // Given
        val mock = everySuspend {
            ledgerTestHelper.apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        }

        // add first supplier
        mock.returns(
            value = Response.success(
                body = ledgerTestHelper.someApiCustomer(
                    name = "John Doe",
                    mobile = "9876543210",
                ),
                rawResponse = ledgerTestHelper.successResponse,
            ),
        )
        val supplier = ledgerTestHelper.customerRepository.addCustomer(
            businessId = "business-id",
            name = "John Doe",
            mobile = "9876543210",
            reactivate = false,
        )

        val error = assertFailsWith<CyclicAccountError> {
            addSupplier.execute(
                name = "John Doe",
                mobile = "9876543210",
                accountType = AccountType.SUPPLIER,
            )
        }

        assertEquals(supplier.id, error.accountId)
    }
}

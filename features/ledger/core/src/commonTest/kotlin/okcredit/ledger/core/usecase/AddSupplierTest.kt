package okcredit.ledger.core.usecase

import app.cash.sqldelight.logs.LogSqliteDriver
import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.usecase.CyclicAccountError
import app.okcredit.ledger.contract.usecase.InvalidNameError
import app.okcredit.ledger.contract.usecase.MobileConflictError
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.usecase.AddAccount
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import okcredit.ledger.core.di.provideDriver
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddSupplierTest {

    private lateinit var addSupplier: AddAccount

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
        addSupplier = AddAccount(
            getActiveBusinessIdLazy = lazyOf(getActiveBusinessId),
            customerRepositoryLazy = lazyOf(customerRepository),
            supplierRepositoryLazy = lazyOf(supplierRepository),
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
            apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(someSupplier(name, mobile), dummyHttpResponse)

        // When
        val supplier = addSupplier.execute(
            name = name,
            mobile = mobile,
            accountType = AccountType.SUPPLIER,
        )

        // Then
        supplierRepository.getSupplierDetails(supplier.id).test {
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
            apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        }

        // add first supplier
        mock.returns(Response.success(someSupplier("John Doe", "9876543210"), dummyHttpResponse))
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
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        }

        // add first supplier
        mock.returns(
            value = Response.success(
                body = someCustomer(
                    name = "John Doe",
                    mobile = "9876543210",
                ),
                rawResponse = dummyHttpResponse,
            ),
        )
        val supplier = customerRepository.addCustomer(
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

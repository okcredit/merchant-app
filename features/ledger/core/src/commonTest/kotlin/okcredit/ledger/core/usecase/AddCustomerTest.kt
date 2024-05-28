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
import app.okcredit.ledger.core.remote.models.ApiCustomer
import app.okcredit.ledger.core.remote.models.ApiSupplier
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
import okcredit.base.randomUUID
import okcredit.ledger.core.di.provideDriver
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AddCustomerTest {

    private lateinit var addCustomer: AddAccount

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
        addCustomer = AddAccount(
            getActiveBusinessIdLazy = lazyOf(getActiveBusinessId),
            customerRepositoryLazy = lazyOf(customerRepository),
            supplierRepositoryLazy = lazyOf(supplierRepository),
        )
    }

    @Test
    fun `add customer with empty name throws error`() = runTest {
        // Given
        val name = ""
        val mobile = "9876543210"
        val profileImage = "https://example.com/image.jpg"
        val reactivate = false

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
        val reactivate = false

        everySuspend {
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        } returns Response.success(someCustomer(name, mobile, profileImage), dummyHttpResponse)

        // When
        val customer = addCustomer.execute(
            name = name,
            mobile = mobile,
            accountType = AccountType.CUSTOMER,
        )

        // Then
        ledgerLocalSource.getCustomerById(customer.id).test {
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
            apiClient.addCustomer(
                request = any(),
                businessId = any(),
            )
        }

        // add first customer
        mock.returns(Response.success(someCustomer("John Doe", "9876543210"), dummyHttpResponse))
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
            apiClient.addSupplier(
                request = any(),
                businessId = any(),
            )
        }

        // add first customer
        mock.returns(
            value = Response.success(
                body = someSupplier(
                    name = "John Doe",
                    mobile = "9876543210",
                ),
                rawResponse = dummyHttpResponse,
            ),
        )
        val supplier = supplierRepository.addSupplier(
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

fun someCustomer(name: String, mobile: String, profileImage: String? = null): ApiCustomer {
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

fun someSupplier(name: String, mobile: String, profileImage: String? = null): ApiSupplier {
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

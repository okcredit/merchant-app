package okcredit.ledger.core.usecase

import app.cash.sqldelight.logs.LogSqliteDriver
import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.local.LedgerLocalSource
import app.okcredit.ledger.core.models.CreateTransaction
import app.okcredit.ledger.core.remote.LedgerApiClient
import app.okcredit.ledger.core.remote.LedgerRemoteSource
import app.okcredit.ledger.core.remote.models.OperationResponseForTransactions
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.syncer.LedgerSyncManager
import app.okcredit.ledger.core.usecase.RecordTransaction
import app.okcredit.ledger.core.usecase.SyncTransactionCommand
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
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
import kotlin.test.assertTrue

class SyncTransactionCommandTest {

    private lateinit var syncTransactionCommand: SyncTransactionCommand

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

    private val recordTransaction = RecordTransaction(
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

    @BeforeTest
    fun setup() {
        syncTransactionCommand = SyncTransactionCommand(
            getActiveBusinessId = getActiveBusinessId,
            localSource = ledgerLocalSource,
            remoteSource = remoteSource,
        )
    }

    @Test
    fun `execute pushes commands to server and mark transactions as synced`() = runTest {
        addSomeBusiness()
        val relationId = addSomeRelation("business-id")
        val first = addSomeTransaction(relationId)
        val second = addSomeTransaction(relationId)

        val commands = ledgerLocalSource.getTransactionCommands(
            businessId = "business-id",
            limit = SyncTransactionCommand.MAX_COMMANDS_TO_SYNC
        )

        everySuspend {
            apiClient.syncTransactions(
                request = any(),
                businessId = any(),
                flowId = any(),
            )
        } returns Response.success(
            body = SyncTransactionResponse(
                operationResponses = commands.map { command ->
                    OperationResponseForTransactions(
                        id = command.id,
                        status = 1,
                    )
                }
            ),
            rawResponse = mock<HttpResponse> {
                every { status } returns HttpStatusCode.OK
            }
        )

        syncTransactionCommand.execute()

        customerRepository.getTransactionDetails(first.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
        customerRepository.getTransactionDetails(second.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
    }

    @Test
    fun `execute retries if we get error from server`() = runTest {
        addSomeBusiness()
        val relationId = addSomeRelation("business-id")
        val first = addSomeTransaction(relationId)
        val second = addSomeTransaction(relationId)

        val commands = ledgerLocalSource.getTransactionCommands(
            businessId = "business-id",
            limit = SyncTransactionCommand.MAX_COMMANDS_TO_SYNC
        )

        everySuspend {
            apiClient.syncTransactions(
                request = any(),
                businessId = any(),
                flowId = any(),
            )
        } sequentially {
            returns(
                Response.error(
                    body = Any(),
                    rawResponse = mock<HttpResponse> {
                        every { status } returns HttpStatusCode.InternalServerError
                    }
                )
            )
            returns(
                Response.success(
                    body = SyncTransactionResponse(
                        operationResponses = commands.map { command ->
                            OperationResponseForTransactions(
                                id = command.id,
                                status = 1,
                            )
                        }
                    ),
                    rawResponse = mock<HttpResponse> {
                        every { status } returns HttpStatusCode.OK
                    }
                )
            )
        }

        syncTransactionCommand.execute()

        customerRepository.getTransactionDetails(first.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
        customerRepository.getTransactionDetails(second.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
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
            }
        )
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
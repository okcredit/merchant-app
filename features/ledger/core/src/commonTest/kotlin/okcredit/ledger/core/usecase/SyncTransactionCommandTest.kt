package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.core.remote.models.OperationResponseForTransactions
import app.okcredit.ledger.core.remote.models.SyncTransactionResponse
import app.okcredit.ledger.core.usecase.SyncTransactionCommand
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.answering.sequentially
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import okcredit.ledger.core.LedgerTestHelper
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SyncTransactionCommandTest {

    private lateinit var syncTransactionCommand: SyncTransactionCommand

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        syncTransactionCommand = SyncTransactionCommand(
            getActiveBusinessId = ledgerTestHelper.getActiveBusinessId,
            localSource = ledgerTestHelper.ledgerLocalSource,
            remoteSource = ledgerTestHelper.remoteSource,
        )
    }

    @Test
    fun `execute pushes commands to server and mark transactions as synced`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)
        val first = ledgerTestHelper.addSomeTransaction(relationId)
        val second = ledgerTestHelper.addSomeTransaction(relationId)

        val commands = ledgerTestHelper.ledgerLocalSource.getTransactionCommands(
            businessId = businessId,
            limit = SyncTransactionCommand.MAX_COMMANDS_TO_SYNC,
        )

        everySuspend {
            ledgerTestHelper.apiClient.syncTransactions(
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
                },
            ),
            rawResponse = mock<HttpResponse> {
                every { status } returns HttpStatusCode.OK
            },
        )

        syncTransactionCommand.execute()

        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(first.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(second.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
    }

    @Test
    fun `execute retries if we get error from server`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()
        val relationId = ledgerTestHelper.addSomeCustomer(businessId)
        val first = ledgerTestHelper.addSomeTransaction(relationId)
        val second = ledgerTestHelper.addSomeTransaction(relationId)

        val commands = ledgerTestHelper.ledgerLocalSource.getTransactionCommands(
            businessId = businessId,
            limit = SyncTransactionCommand.MAX_COMMANDS_TO_SYNC,
        )

        everySuspend {
            ledgerTestHelper.apiClient.syncTransactions(
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
                    },
                ),
            )
            returns(
                Response.success(
                    body = SyncTransactionResponse(
                        operationResponses = commands.map { command ->
                            OperationResponseForTransactions(
                                id = command.id,
                                status = 1,
                            )
                        },
                    ),
                    rawResponse = mock<HttpResponse> {
                        every { status } returns HttpStatusCode.OK
                    },
                ),
            )
        }

        syncTransactionCommand.execute()

        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(first.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(second.id).test {
            val item = awaitItem()
            assertEquals(false, item?.dirty)
        }
    }
}

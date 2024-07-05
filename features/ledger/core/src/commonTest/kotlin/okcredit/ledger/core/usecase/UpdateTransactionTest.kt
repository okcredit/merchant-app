package okcredit.ledger.core.usecase

import app.cash.turbine.test
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.model.UpdateTransactionRequest
import app.okcredit.ledger.core.models.DeleteTransaction
import app.okcredit.ledger.core.usecase.UpdateTransaction
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

class UpdateTransactionTest {

    private lateinit var updateTransaction: UpdateTransaction

    private val ledgerTestHelper = LedgerTestHelper()

    @BeforeTest
    fun setup() {
        updateTransaction = UpdateTransaction(
            ledgerLocalSource = ledgerTestHelper.ledgerLocalSource,
            ledgerSyncManager = ledgerTestHelper.ledgerSyncManager,
        )
    }

    @Test
    fun `execute should update note for merchant transaction`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()

        val customerId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = ledgerTestHelper.addSomeTransaction(customerId)

        // Given
        val updateTransactionRequest = UpdateTransactionRequest.UpdateNote(
            transactionId = transaction.id,
            accountType = AccountType.CUSTOMER,
            note = "new-note",
        )

        // When
        updateTransaction.execute(updateTransactionRequest)

        // Then
        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(transaction.id).test {
            assertEquals("new-note", awaitItem()?.note)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `execute should update amount for merchant transaction`() = runTest {
        val newAmount = (100..1000).random().times(100).paisa

        val businessId = ledgerTestHelper.addSomeBusiness()

        val customerId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = ledgerTestHelper.addSomeTransaction(customerId)

        val existingBalance = ledgerTestHelper.ledgerLocalSource.getCustomerById(customerId).firstOrNull()?.balance

        // Given
        val updateTransactionRequest = UpdateTransactionRequest.UpdateAmount(
            transactionId = transaction.id,
            amount = newAmount,
            accountType = AccountType.CUSTOMER,
        )

        // When
        updateTransaction.execute(updateTransactionRequest)

        // Then
        ledgerTestHelper.ledgerLocalSource.getTransactionDetails(transaction.id).test {
            assertEquals(newAmount, awaitItem()?.amount)

            cancelAndIgnoreRemainingEvents()
        }

        val newBalance = if (transaction.type == Transaction.Type.CREDIT) {
            existingBalance?.minus(transaction.amount)?.plus(newAmount)
        } else {
            existingBalance?.plus(transaction.amount)?.minus(newAmount)
        }

        ledgerTestHelper.ledgerLocalSource.getCustomerById(customerId).test {
            assertEquals(newBalance, awaitItem()?.balance)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `update amount throws error if transaction does not exist`() = runTest {
        val updateTransactionRequest = UpdateTransactionRequest.UpdateAmount(
            transactionId = randomUUID(),
            amount = 1000.paisa,
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }

    @Test
    fun `update note throws error if transaction does not exist`() = runTest {
        val updateTransactionRequest = UpdateTransactionRequest.UpdateNote(
            transactionId = randomUUID(),
            note = "new-note",
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }

    @Test
    fun `update amount throws error if transaction is not created by merchant`() = runTest {
        val transactionId = randomUUID()
        ledgerTestHelper.ledgerLocalSource.insertTransactions(
            listOf(
                Transaction(
                    id = transactionId,
                    businessId = "business-id",
                    accountId = "account-id",
                    amount = 1000.paisa,
                    dirty = true,
                    type = Transaction.Type.CREDIT,
                    note = "note",
                    billDate = Clock.System.now().timestamp,
                    createdAt = Clock.System.now().timestamp,
                    createdByCustomer = true,
                ),
            ),
        )

        val updateTransactionRequest = UpdateTransactionRequest.UpdateAmount(
            transactionId = transactionId,
            amount = 1000.paisa,
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }

    @Test
    fun `update note throws error if transaction is not created by merchant`() = runTest {
        val transactionId = randomUUID()
        ledgerTestHelper.ledgerLocalSource.insertTransactions(
            listOf(
                Transaction(
                    id = transactionId,
                    businessId = "business-id",
                    accountId = "account-id",
                    amount = 1000.paisa,
                    dirty = true,
                    type = Transaction.Type.CREDIT,
                    note = "note",
                    billDate = Clock.System.now().timestamp,
                    createdAt = Clock.System.now().timestamp,
                    createdByCustomer = true,
                ),
            ),
        )

        val updateTransactionRequest = UpdateTransactionRequest.UpdateNote(
            transactionId = transactionId,
            note = "new-note",
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }

    @Test
    fun `update amount throws error if transaction is deleted`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()

        val customerId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = ledgerTestHelper.addSomeTransaction(customerId)

        ledgerTestHelper.ledgerLocalSource.deleteTransaction(
            transaction = transaction,
            command = DeleteTransaction(
                id = randomUUID(),
                transactionId = transaction.id,
                accountType = AccountType.CUSTOMER,
                createTime = Clock.System.now().timestamp,
            ),
        )

        val updateTransactionRequest = UpdateTransactionRequest.UpdateAmount(
            transactionId = transaction.id,
            amount = 1000.paisa,
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }

    @Test
    fun `update note throws error if transaction is deleted`() = runTest {
        val businessId = ledgerTestHelper.addSomeBusiness()

        val customerId = ledgerTestHelper.addSomeCustomer(businessId)

        val transaction = ledgerTestHelper.addSomeTransaction(customerId)

        ledgerTestHelper.ledgerLocalSource.deleteTransaction(
            transaction = transaction,
            command = DeleteTransaction(
                id = randomUUID(),
                transactionId = transaction.id,
                accountType = AccountType.CUSTOMER,
                createTime = Clock.System.now().timestamp,
            ),
        )

        val updateTransactionRequest = UpdateTransactionRequest.UpdateNote(
            transactionId = transaction.id,
            note = "new-note",
            accountType = AccountType.CUSTOMER,
        )

        assertFailsWith<IllegalArgumentException> {
            updateTransaction.execute(updateTransactionRequest)
        }
    }
}

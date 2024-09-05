@file:OptIn(ExperimentalCoroutinesApi::class)

package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.ui.composable.TxnGravity
import app.okcredit.ledger.ui.composable.UiTxnStatus
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.TransactionData
import app.okcredit.ledger.ui.model.TransactionDueInfo
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSameDay
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSevenDaysPassed
import app.okcredit.ledger.ui.utils.StringUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.differenceInDays
import okcredit.base.units.instant
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import kotlin.math.absoluteValue


@Inject
class GetCustomerLedgerData(
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
    customerRepository: Lazy<CustomerRepository>,
    getAccountStatement: Lazy<GetAccountStatementImpl>
) {

    private val getActiveBusinessId by lazy { getActiveBusinessId.value }
    private val customerRepository by lazy { customerRepository.value }
    private val getAccountStatement by lazy { getAccountStatement.value }

    fun execute(customerId: String, showOldClicked: Boolean): Flow<Response> {
        if (customerId.isEmpty()) throw IllegalArgumentException("Customer Id is empty")
        return flow {
            val businessId = getActiveBusinessId.execute()
            emit(businessId)
        }.flatMapLatest {
            combine(
                getCustomerDetails(customerId),
                getCustomerStatement(customerId)
            ) { customer, transactions ->
                processTransactionsData(
                    customer = customer,
                    transactions = transactions.reversed(),
                    showOldClicked = showOldClicked
                )
            }
        }
    }

    private fun processTransactionsData(
        customer: Customer?,
        transactions: List<Transaction>,
        showOldClicked: Boolean
    ): Response {
        val list = mutableListOf<LedgerItem>()
        if (transactions.isEmpty() && customer == null) {
            return Response(getEmptyCustomerPlaceHolderItem(name = customer?.name ?: ""))
        }

        val transactionInfo = processTransactionAndCurrentDue(transactions)

        val startIndex = if (!showOldClicked
            && transactionInfo.lastIndexOfZeroBalanceDue > 0
        ) {
            list.add(LedgerItem.LoadMoreItem)
            transactionInfo.lastIndexOfZeroBalanceDue + 1
        } else {
            0
        }

        var lastTransactionDate = 0L
        val txns = transactionInfo.transactions
        for (index in startIndex until transactions.size) {
            val transaction = txns[index].transaction
            val currentTransactionDate = transaction.billDate

            lastTransactionDate = checkForDateItem(
                list = list,
                lastDate = lastTransactionDate,
                currentDate = currentTransactionDate
            )
            addTransactionItemInList(
                list = list,
                transaction = transaction,
                customerId = customer?.id!!,
                customerName = customer.name,
                closingBalance = txns[index].currentDue
            )
        }
        return Response(list)
    }

    private fun processTransactionAndCurrentDue(
        transactions: List<Transaction>,
    ): TransactionData {
        var currentDue: Paisa = Paisa.ZERO
        var lastIndexOfZeroBalanceDue = 0
        val transactionWithCurrentDue = transactions.mapIndexed { index, transaction ->
            currentDue = when (transaction.type) {
                Transaction.Type.CREDIT -> if (transaction.deleted) currentDue else currentDue.minus(
                    transaction.amount
                )

                Transaction.Type.PAYMENT -> if (transaction.deleted) currentDue else currentDue.plus(
                    transaction.amount
                )

                else -> currentDue
            }

            if (currentDue == Paisa.ZERO &&
                !transaction.deleted &&
                index != transactions.size - 1
            ) {
                lastIndexOfZeroBalanceDue = index
            }
            TransactionDueInfo(
                transaction = transaction,
                currentDue = currentDue
            )
        }

        return TransactionData(
            transactions = transactionWithCurrentDue,
            lastIndexOfZeroBalanceDue = lastIndexOfZeroBalanceDue
        )
    }

    private fun addTransactionItemInList(
        list: MutableList<LedgerItem>,
        transaction: Transaction,
        customerId: String,
        customerName: String,
        closingBalance: Paisa
    ) {
        when {
            isDeletedTransaction(transaction) -> list.add(
                createDeletedTransaction(
                    transaction = transaction,
                    customerId = customerId,
                    customerName = customerName,
                    closingBalance = closingBalance
                )
            )

            isProcessTransaction(transaction) -> list.add(
                createProcessingTransaction(
                    transaction = transaction,
                    customerId = customerId,
                    closingBalance = closingBalance
                )
            )

            else -> list.add(
                createTransactionItem(
                    transaction = transaction,
                    customerId = customerId,
                    customerName = customerName,
                    closingBalance = closingBalance
                )
            )
        }
    }

    private fun createTransactionItem(
        transaction: Transaction,
        customerId: String,
        customerName: String,
        closingBalance: Paisa
    ): LedgerItem {
        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.Customer
            ),
            amount = transaction.amount,
            date = transaction.billDate.relativeTime(),
            dirty = transaction.dirty,
            createdBySelf = transaction.createdByMerchant,
            isDiscountTransaction = transaction.category == Transaction.Category.DISCOUNT,
            note = transaction.note,
            txnTag = findTransactionTag(transaction, customerName = customerName),
            closingBalance = closingBalance,
            relationshipId = customerId,
            collectionId = "",
        )
    }

    private fun findTransactionTag(transaction: Transaction, customerName: String?): String? {
        return when {
            transaction.category == Transaction.Category.DISCOUNT && transaction.deleted -> {
                "Discount deleted"
            }

            transaction.category == Transaction.Category.AUTO_CREDIT -> {
                "Subscription"
            }

            transaction.category == Transaction.Category.DISCOUNT -> {
                "Discount offered"
            }

            transaction.amountUpdated -> {
                "Edited"
            }

            transaction.createdByCustomer -> {
                "Added by ${StringUtils.getShortName(customerName)}"
            }

            else -> {
                null
            }
        }
    }

    private fun createProcessingTransaction(
        transaction: Transaction,
        customerId: String,
        closingBalance: Paisa
    ): LedgerItem {
        val action = if (transaction.billDate.differenceInDays().absoluteValue > 7) {
            UiTxnStatus.ProcessingTransactionAction.NONE
        } else {
            UiTxnStatus.ProcessingTransactionAction.HELP
        }

        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.Customer
            ),
            amount = transaction.amount,
            date = transaction.billDate.relativeTime(),
            closingBalance = closingBalance,
            txnTag = "Online Transaction",
            txnType = UiTxnStatus.ProcessingTransaction(
                action = action,
            ),
            note = transaction.note,
            relationshipId = customerId,
            dirty = transaction.dirty,
            collectionId = ""
        )
    }

    private fun isProcessTransaction(transaction: Transaction): Boolean {
        return transaction.state == Transaction.State.PROCESSING
    }

    private fun isDeletedTransaction(transaction: Transaction) =
        transaction.deleted && transaction.category != Transaction.Category.DISCOUNT

    private fun createDeletedTransaction(
        transaction: Transaction,
        customerId: String,
        customerName: String,
        closingBalance: Paisa,
    ): LedgerItem.TransactionItem {
        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            txnType = UiTxnStatus.DeletedTransaction(
                isDeletedByCustomer = transaction.deletedByCustomer,
            ),
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.Customer
            ),
            dirty = transaction.dirty,
            amount = transaction.amount,
            date = transaction.billDate.relativeTime(),
            closingBalance = closingBalance,
            relationshipId = customerId,
            note = transaction.note,
            txnTag = getDeletedTransactionTag(
                customerName,
                transaction.deletedByCustomer,
            ),
            collectionId = ""
        )
    }

    private fun getDeletedTransactionTag(
        customerName: String,
        deletedByCustomer: Boolean
    ): String {
        return if (deletedByCustomer) {
            "Deleted by $customerName"
        } else {
            ""
        }
    }

    private fun findUiTxnGravity(isPayment: Boolean, accountType: AccountType): TxnGravity {
        return if (isPayment) {
            if (accountType == AccountType.Customer) TxnGravity.LEFT else TxnGravity.RIGHT
        } else {
            if (accountType == AccountType.Customer) TxnGravity.RIGHT else TxnGravity.LEFT
        }
    }

    private fun checkForDateItem(
        list: MutableList<LedgerItem>,
        lastDate: Long,
        currentDate: Timestamp
    ): Long {
        return if (!isSameDay(lastDate, currentDate.epochMillis)) {
            list.add(LedgerItem.DateItem(currentDate.relativeDate()))
            currentDate.epochMillis
        } else {
            lastDate
        }
    }

    private fun getEmptyCustomerPlaceHolderItem(name: String) = listOf(LedgerItem.EmptyPlaceHolder(name))

    private fun getCustomerDetails(customerId: String) =
        customerRepository.getCustomerDetails(customerId)

    private fun getCustomerStatement(customerId: String): Flow<List<Transaction>> {
        return getAccountStatement.execute(customerId)
    }

    data class Response(
        val customerLedgerItems: List<LedgerItem>,
    )
}
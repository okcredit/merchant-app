@file:OptIn(ExperimentalCoroutinesApi::class)

package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.ui.composables.TxnGravity
import app.okcredit.ledger.ui.composables.UiTxnStatus
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.TransactionData
import app.okcredit.ledger.ui.model.TransactionDueInfo
import app.okcredit.ledger.ui.utils.DateTimeUtils.formatDateOnly
import app.okcredit.ledger.ui.utils.DateTimeUtils.getTimeOnly
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSameDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.instant
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId


@Inject
class GetCustomerLedgerData(
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
    customerRepository: Lazy<CustomerRepository>,
    getAccountStatement: Lazy<GetAccountStatementImpl>
) {

    private val getActiveBusinessId by lazy { getActiveBusinessId.value }
    private val customerRepository by lazy { customerRepository.value }
    private val getAccountStatement by lazy { getAccountStatement.value }

    fun execute(customerId: String, showOldClicked: Boolean) {
        flow {
            val businessId = getActiveBusinessId.execute()
            emit(businessId)
        }.flatMapLatest {
            combine(
                getCustomerDetails(customerId),
                getCustomerStatement(customerId)
            ) { customer, transactions ->
                processTransactionsData(
                    customer = customer,
                    transactions = transactions,
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
                currentDate = currentTransactionDate.epochMillis
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
        }
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
                transaction.type == Transaction.Type.PAYMENT,
                AccountType.Customer
            ),
            isDirty = transaction.dirty,
            amount = transaction.amount,
            date = findFormattedDateOrTime(
                createdAt = transaction.createdAt,
                billDate = transaction.billDate,
            ),
            closingBalance = closingBalance,
            relationshipId = customerId,
            note = transaction.note,
            txnTag = getDeletedTransactionTag(
                customerName,
                transaction.deletedByCustomer,
            ),
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

    private fun findFormattedDateOrTime(
        createdAt: Timestamp,
        billDate: Timestamp,
    ): String {
        return if (isSameDay(
                lastDate = createdAt.epochMillis,
                currentDate = billDate.epochMillis
            )
        ) {
            getTimeOnly(billDate.instant).trim()
        } else {
            formatDateOnly(billDate.instant).trim()
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
        currentDate: Long
    ): Long {
        return if (!isSameDay(lastDate, currentDate)) {
            list.add(
                LedgerItem.DateItem(
                    ""
                )
            )
            currentDate
        } else {
            lastDate
        }
    }


    private fun getEmptyCustomerPlaceHolderItem(name: String) = listOf(
        LedgerItem.EmptyPlaceHolder(
            name
        )
    )

    private fun getCustomerDetails(customerId: String) =
        customerRepository.getCustomerDetails(customerId)

    private fun getCustomerStatement(customerId: String): Flow<List<Transaction>> {
        return getAccountStatement.execute(customerId)
    }

    data class Response(
        val customerLedgerItems: List<LedgerItem>,
    )
}
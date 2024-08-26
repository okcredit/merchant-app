@file:OptIn(ExperimentalCoroutinesApi::class)

package app.okcredit.ledger.ui.supplier.usecase

import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.ui.composable.TxnGravity
import app.okcredit.ledger.ui.composable.UiTxnStatus
import app.okcredit.ledger.ui.model.AccountType
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.TransactionData
import app.okcredit.ledger.ui.model.TransactionDueInfo
import app.okcredit.ledger.ui.utils.DateTimeUtils.formatDateOnly
import app.okcredit.ledger.ui.utils.DateTimeUtils.getTimeOnly
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSameDay
import app.okcredit.ledger.ui.utils.StringUtils.getShortName
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
class GetSupplierLedgerData(
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
    supplierRepository: Lazy<SupplierRepository>,
    getAccountStatement: Lazy<GetAccountStatementImpl>
) {

    private val getActiveBusinessId by lazy { getActiveBusinessId.value }
    private val supplierRepository by lazy { supplierRepository.value }
    private val getAccountStatement by lazy { getAccountStatement.value }

    fun execute(supplierId: String, showOldClicked: Boolean): Flow<List<LedgerItem>> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emit(businessId)
        }.flatMapLatest {
            combine(
                getSupplierDetails(supplierId),
                getSupplierTransactions(supplierId)
            ) { supplier, transactions ->
                processTransactionsData(
                    supplier = supplier,
                    transactions = transactions,
                    showOldClicked = showOldClicked
                )
            }
        }
    }

    private fun processTransactionsData(
        supplier: Supplier?,
        transactions: List<Transaction>,
        showOldClicked: Boolean
    ): List<LedgerItem> {
        val ledgerItems = mutableListOf<LedgerItem>()
        if (supplier == null && transactions.isEmpty()) {
            ledgerItems.add(LedgerItem.EmptyPlaceHolder(name = supplier?.name ?: ""))
            return ledgerItems
        }

        val transactionInfo = processTransactionDueInfo(transactions)

        val startIndex = if (!showOldClicked && transactionInfo.lastIndexOfZeroBalanceDue > 0) {
            ledgerItems.add(LedgerItem.LoadMoreItem)
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
                ledgerItems = ledgerItems,
                lastDate = lastTransactionDate,
                currentDate = currentTransactionDate.epochMillis
            )
            addTransactionItemInList(
                ledgerItems = ledgerItems,
                transaction = transaction,
                supplierId = supplier?.id!!,
                supplierName = supplier.name,
                closingBalance = txns[index].currentDue
            )
        }
        return ledgerItems
    }

    private fun addTransactionItemInList(
        ledgerItems: MutableList<LedgerItem>,
        transaction: Transaction,
        supplierId: String,
        supplierName: String,
        closingBalance: Paisa
    ) {
        when {
            isDeletedTransaction(transaction) -> ledgerItems.add(
                createDeletedTransaction(
                    transaction = transaction,
                    supplierId = supplierId,
                    supplierName = supplierName,
                    closingBalance = closingBalance
                )
            )

            isProcessingTransaction(transaction) -> ledgerItems.add(
                createProcessingTransaction(
                    transaction = transaction,
                    supplierId = supplierId,
                    supplierName = supplierName,
                    closingBalance = closingBalance
                )
            )

            else -> ledgerItems.add(
                createTransaction(
                    transaction = transaction,
                    supplierId = supplierId,
                    supplierName = supplierName,
                    closingBalance = closingBalance
                )
            )
        }
    }

    private fun createProcessingTransaction(
        transaction: Transaction,
        supplierId: String,
        supplierName: String,
        closingBalance: Paisa
    ): LedgerItem {
        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            relationshipId = supplierId,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.Supplier
            ),
            dirty = transaction.dirty,
            createdBySelf = transaction.createdByMerchant,
            amount = transaction.amount,
            date = findFormattedDateOrTime(
                createdAt = transaction.createdAt,
                billDate = transaction.billDate,
            ),
            note = transaction.note,
            closingBalance = closingBalance,
            txnType = UiTxnStatus.ProcessingTransaction(
                action = UiTxnStatus.ProcessingTransactionAction.NONE,
            ),
            accountType = AccountType.Supplier,
            txnTag = getTransactionTag(
                createdByMerchant = transaction.createdByMerchant,
                name = supplierName
            )
        )
    }

    private fun isProcessingTransaction(transaction: Transaction): Boolean {
        return transaction.state == Transaction.State.PROCESSING
    }

    private fun createTransaction(
        transaction: Transaction,
        supplierId: String,
        supplierName: String,
        closingBalance: Paisa
    ): LedgerItem {
        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            relationshipId = supplierId,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.Supplier
            ),
            dirty = transaction.dirty,
            createdBySelf = transaction.createdByMerchant,
            amount = transaction.amount,
            date = findFormattedDateOrTime(
                createdAt = transaction.createdAt,
                billDate = transaction.billDate,
            ),
            note = transaction.note,
            closingBalance = closingBalance,
            txnType = UiTxnStatus.Transaction,
            accountType = AccountType.Supplier,
            txnTag = getTransactionTag(
                createdByMerchant = transaction.createdByMerchant,
                name = supplierName
            )
        )
    }

    private fun getTransactionTag(createdByMerchant: Boolean, name: String): String {
        return when {
            !createdByMerchant -> "Added by ${getShortName(name)}"
            else -> ""
        }
    }

    private fun createDeletedTransaction(
        transaction: Transaction,
        supplierId: String,
        supplierName: String,
        closingBalance: Paisa
    ): LedgerItem {
        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            relationshipId = supplierId,
            txnGravity = findUiTxnGravity(
                transaction.type == Transaction.Type.PAYMENT,
                AccountType.Supplier
            ),
            dirty = transaction.dirty,
            createdBySelf = transaction.createdByMerchant,
            amount = transaction.amount,
            date = findFormattedDateOrTime(
                createdAt = transaction.createdAt,
                billDate = transaction.billDate,
            ),
            note = transaction.note,
            closingBalance = closingBalance,
            txnType = UiTxnStatus.DeletedTransaction(
                isDeletedByCustomer = transaction.deletedByCustomer,
            ),
            txnTag = getDeletedTransactionTag(
                customerName = supplierName,
                deletedByCustomer = transaction.deletedByCustomer
            ),
            accountType = AccountType.Supplier,
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

    private fun isDeletedTransaction(transaction: Transaction) = transaction.deleted

    private fun checkForDateItem(
        ledgerItems: MutableList<LedgerItem>,
        lastDate: Long,
        currentDate: Long
    ): Long {
        return if (!isSameDay(lastDate, currentDate)) {
            ledgerItems.add(
                LedgerItem.DateItem(
                    ""
                )
            )
            currentDate
        } else {
            lastDate
        }
    }

    private fun processTransactionDueInfo(transactions: List<Transaction>): TransactionData {
        var currentDue: Paisa = Paisa.ZERO
        var lastIndexOfZeroBalanceDue = 0
        val transactionWithDueInfo = transactions.mapIndexed { index, txn ->
            currentDue = when {
                txn.state == Transaction.State.PROCESSING -> currentDue
                txn.type == Transaction.Type.PAYMENT -> if (txn.deleted) currentDue else currentDue - txn.amount
                txn.type == Transaction.Type.CREDIT -> if (txn.deleted) currentDue else currentDue + txn.amount
                else -> currentDue
            }

            if (currentDue == Paisa.ZERO && !txn.deleted && index != transactions.size - 1) {
                lastIndexOfZeroBalanceDue = index
            }
            TransactionDueInfo(
                transaction = txn,
                currentDue = currentDue
            )
        }

        return TransactionData(
            transactions = transactionWithDueInfo,
            lastIndexOfZeroBalanceDue = lastIndexOfZeroBalanceDue
        )
    }


    private fun getSupplierTransactions(supplierId: String): Flow<List<Transaction>> {
        return getAccountStatement.execute(
            accountId = supplierId,
            startTime = null,
            endTime = null
        )
    }

    private fun getSupplierDetails(supplierId: String): Flow<Supplier?> {
        return supplierRepository.getSupplierDetails(supplierId)
    }

}
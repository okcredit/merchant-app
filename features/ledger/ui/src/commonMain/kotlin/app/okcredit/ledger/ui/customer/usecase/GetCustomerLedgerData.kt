@file:OptIn(ExperimentalCoroutinesApi::class)

package app.okcredit.ledger.ui.customer.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.usecase.GetAccountStatementImpl
import app.okcredit.ledger.ui.composable.TxnGravity
import app.okcredit.ledger.ui.composable.UiTxnStatus
import app.okcredit.ledger.ui.model.LedgerItem
import app.okcredit.ledger.ui.model.TransactionData
import app.okcredit.ledger.ui.model.TransactionDueInfo
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSameDay
import app.okcredit.ledger.ui.utils.DateTimeUtils.isSevenDaysPassed
import app.okcredit.ledger.ui.utils.StringUtils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import okcredit.base.units.instant
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.collection.model.CollectionStatus
import tech.okcredit.collection.model.KycStatus
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.collection.usecase.GetCollectionsForAccount
import tech.okcredit.collection.usecase.GetMerchantCollectionProfile
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId


@Inject
class GetCustomerLedgerData(
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
    customerRepository: Lazy<CustomerRepository>,
    getAccountStatement: Lazy<GetAccountStatementImpl>,
    getCollectionForAccount: Lazy<GetCollectionsForAccount>,
    getMerchantCollectionProfile: Lazy<GetMerchantCollectionProfile>
) {

    private val getActiveBusinessId by lazy { getActiveBusinessId.value }
    private val customerRepository by lazy { customerRepository.value }
    private val getAccountStatement by lazy { getAccountStatement.value }
    private val customerCollections by lazy { getCollectionForAccount.value }
    private val merchantCollectionProfile by lazy { getMerchantCollectionProfile.value }

    fun execute(customerId: String, showOldClicked: Boolean): Flow<Response> {
        if (customerId.isEmpty()) throw IllegalArgumentException("Customer Id is empty")
        return flow {
            val businessId = getActiveBusinessId.execute()
            emit(businessId)
        }.flatMapLatest {
            combine(
                getCustomerDetails(customerId),
                getCustomerStatement(customerId),
                getCustomerCollectionProfile(businessId = it, accountId = customerId)
            ) { customer, transactions, onlinePayments ->
                processTransactionsData(
                    customer = customer,
                    onlinePayments = onlinePayments,
                    transactions = transactions,
                    showOldClicked = showOldClicked
                )
            }
        }
    }

    private fun getCustomerCollectionProfile(
        businessId: String,
        accountId: String
    ): Flow<List<OnlinePayment>> {
        return customerCollections.execute(businessId, accountId)
    }

    private suspend fun processTransactionsData(
        customer: Customer?,
        transactions: List<Transaction>,
        showOldClicked: Boolean,
        onlinePayments: List<OnlinePayment>
    ): Response {
        val list = mutableListOf<LedgerItem>()
        if (transactions.isEmpty() && customer == null) {
            return Response(getEmptyCustomerPlaceHolderItem(name = customer?.name ?: ""))
        }
        val collectionMap = onlinePayments.associateBy { it.id }

        val transactionInfo = processTransactionAndCurrentDue(transactions, collectionMap)

        val startIndex = if (!showOldClicked
            && transactionInfo.lastIndexOfZeroBalanceDue > 0
        ) {
            list.add(LedgerItem.LoadMoreItem)
            transactionInfo.lastIndexOfZeroBalanceDue + 1
        } else {
            0
        }
        val collectionMerchantProfile = merchantCollectionProfile.execute().first()

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
                collectionMap = collectionMap,
                transaction = transaction,
                customerId = customer?.id!!,
                customerName = customer.name,
                closingBalance = txns[index].currentDue,
                collectionMerchantProfile = collectionMerchantProfile
            )
        }
        return Response(list)
    }

    private fun processTransactionAndCurrentDue(
        transactions: List<Transaction>,
        collectionMap: Map<String, OnlinePayment>
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
                currentDue = currentDue,
                onlinePayment = collectionMap[transaction.collectionId]
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
        closingBalance: Paisa,
        collectionMap: Map<String, OnlinePayment>,
        collectionMerchantProfile: CollectionMerchantProfile
    ) {
        when {
            isDeletedTransaction(transaction) -> list.add(
                createDeletedTransaction(
                    collectionMap = collectionMap,
                    transaction = transaction,
                    customerId = customerId,
                    customerName = customerName,
                    closingBalance = closingBalance
                )
            )

            isProcessTransaction(transaction) -> list.add(
                createProcessingTransaction(
                    collectionMap = collectionMap,
                    transaction = transaction,
                    customerId = customerId,
                    closingBalance = closingBalance,
                    collectionMerchantProfile = collectionMerchantProfile
                )
            )

            else -> list.add(
                createTransactionItem(
                    collectionMap = collectionMap,
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
        closingBalance: Paisa,
        collectionMap: Map<String, OnlinePayment>
    ): LedgerItem {
        val collectionId = transaction.collectionId
        val collection = collectionMap[collectionId]
        val collectionStatus = collection?.status

        val pendingOnlineTransaction = if (!collectionId.isNullOrEmpty()) {
            collectionStatus == null ||
                    collectionStatus == CollectionStatus.PAID ||
                    collectionStatus == CollectionStatus.PAYOUT_INITIATED
        } else false

        val platformFee = collection?.platformFee ?: 0L

        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.CUSTOMER
            ),
            amount = transaction.amount,
            date = transaction.billDate.relativeTime(),
            dirty = transaction.dirty || pendingOnlineTransaction,
            createdBySelf = transaction.createdByMerchant,
            isDiscountTransaction = transaction.category == Transaction.Category.DISCOUNT,
            note = getTransactionNote(
                note = transaction.note,
                platformFee = platformFee
            ),
            txnTag = findTransactionTag(transaction, customerName = customerName),
            closingBalance = closingBalance,
            relationshipId = customerId,
            collectionId = collectionId,
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
        closingBalance: Paisa,
        collectionMap: Map<String, OnlinePayment>,
        collectionMerchantProfile: CollectionMerchantProfile
    ): LedgerItem {
        val collectionId = transaction.collectionId

        var action = if (isSevenDaysPassed(transaction.billDate.instant)) {
            UiTxnStatus.ProcessingTransactionAction.NONE
        } else {
            UiTxnStatus.ProcessingTransactionAction.HELP
        }

        when {
            collectionMerchantProfile.remainingLimit <= 0 && isPaymentTxn(transaction) -> {
                action = if (collectionMerchantProfile.kycStatus == KycStatus.COMPLETE.name) {
                    UiTxnStatus.ProcessingTransactionAction.NONE
                } else {
                    UiTxnStatus.ProcessingTransactionAction.KYC
                }
            }
        }

        val paymentId = if (!collectionId.isNullOrEmpty()) {
            collectionMap[transaction.collectionId]?.paymentId ?: ""
        } else {
            ""
        }

        return LedgerItem.TransactionItem(
            txnId = transaction.id,
            txnGravity = findUiTxnGravity(
                isPayment = transaction.type == Transaction.Type.PAYMENT,
                accountType = AccountType.CUSTOMER
            ),
            amount = transaction.amount,
            date = transaction.billDate.relativeTime(),
            closingBalance = closingBalance,
            txnTag = "Online Transaction",
            txnType = UiTxnStatus.ProcessingTransaction(
                action = action,
                paymentId = paymentId
            ),
            note = transaction.note,
            relationshipId = customerId,
            dirty = transaction.dirty,
            collectionId = collectionId,
        )
    }

    private fun isPaymentTxn(transaction: Transaction) =
        transaction.type == Transaction.Type.PAYMENT

    private fun getTransactionNote(note: String?, platformFee: Long): String? {
        return if (note.isNullOrEmpty() && platformFee > 0L) {
            "Platform Fee Applied"
        } else {
            note
        }
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
        collectionMap: Map<String, OnlinePayment>,
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
            if (accountType == AccountType.CUSTOMER) TxnGravity.LEFT else TxnGravity.RIGHT
        } else {
            if (accountType == AccountType.CUSTOMER) TxnGravity.RIGHT else TxnGravity.LEFT
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

    private fun getEmptyCustomerPlaceHolderItem(name: String) =
        listOf(LedgerItem.EmptyPlaceHolder(name))

    private fun getCustomerDetails(customerId: String) =
        customerRepository.getCustomerDetails(customerId)

    private fun getCustomerStatement(customerId: String): Flow<List<Transaction>> {
        return getAccountStatement.execute(customerId)
    }

    data class Response(
        val customerLedgerItems: List<LedgerItem>,
    )
}
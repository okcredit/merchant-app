package app.okcredit.ledger.core

import app.okcredit.ledger.contract.Ledger
import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.model.UpdateTransactionRequest
import app.okcredit.ledger.core.usecase.AddAccount
import app.okcredit.ledger.core.usecase.DeleteCustomer
import app.okcredit.ledger.core.usecase.DeleteTransaction
import app.okcredit.ledger.core.usecase.RecordTransaction
import me.tatarka.inject.annotations.Inject
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class LedgerImpl(
    recordTransactionLazy: Lazy<RecordTransaction>,
    addAccountLazy: Lazy<AddAccount>,
    deleteCustomerLazy: Lazy<DeleteCustomer>,
    deleteTransactionLazy: Lazy<DeleteTransaction>,
) : Ledger {

    private val recordTransaction by lazy { recordTransactionLazy.value }
    private val addAccount by lazy { addAccountLazy.value }
    private val deleteCustomer by lazy { deleteCustomerLazy.value }
    private val deleteTransaction by lazy { deleteTransactionLazy.value }

    override suspend fun recordTransaction(
        accountId: String,
        accountType: AccountType,
        amount: Paisa,
        type: Transaction.Type,
        billDate: Timestamp,
        transactionId: String,
        note: String?,
        bills: List<String>,
    ): Transaction {
        return recordTransaction.execute(
            accountId = accountId,
            accountType = accountType,
            amount = amount,
            type = type,
            billDate = billDate,
            transactionId = transactionId,
            note = note,
            bills = bills,
        )
    }

    override suspend fun updateTransaction(request: UpdateTransactionRequest) {
    }

    override suspend fun deleteTransaction(transactionId: String, accountType: AccountType) {
        deleteTransaction.execute(transactionId, accountType)
    }

    override suspend fun deleteAccount(accountId: String, accountType: AccountType) {
        if (accountType == AccountType.CUSTOMER) {
            deleteCustomer.execute(accountId)
        }
    }

    override suspend fun addAccount(
        name: String,
        mobile: String?,
        accountType: AccountType,
    ): Account {
        return addAccount.execute(
            name = name,
            mobile = mobile,
            accountType = accountType,
        )
    }
}

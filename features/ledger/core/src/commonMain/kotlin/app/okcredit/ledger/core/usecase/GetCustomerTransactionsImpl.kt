package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.Transaction
import app.okcredit.ledger.contract.model.isCustomer
import app.okcredit.ledger.contract.usecase.GetTransactionsForAccount
import app.okcredit.ledger.core.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class GetCustomerTransactionsImpl(
    private val repository: CustomerRepository,

) : GetTransactionsForAccount {
    override fun execute(accountId: String, accountType: AccountType): Flow<List<Transaction>> {
        return if (accountType.isCustomer()) {
            repository.getTransactionsForCustomer(accountId)
        } else {
            flowOf()
        }
    }
}

package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Account
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.isCustomer
import app.okcredit.ledger.contract.usecase.GetAccount
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class GetAccountImpl(
    private val customerRepository: CustomerRepository,
    private val supplierRepository: SupplierRepository,
) : GetAccount {

    override fun execute(accountId: String, accountType: AccountType): Flow<Account?> {
        return if (accountType.isCustomer()) {
            customerRepository.getCustomerDetails(accountId)
        } else {
            supplierRepository.getSupplierDetails(accountId)
        }
    }
}

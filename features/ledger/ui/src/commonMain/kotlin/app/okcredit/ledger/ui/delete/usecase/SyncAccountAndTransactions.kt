package app.okcredit.ledger.ui.delete.usecase

import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.isSupplier
import app.okcredit.ledger.core.CustomerRepository
import app.okcredit.ledger.core.SupplierRepository
import app.okcredit.ledger.core.usecase.SyncTransactions
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class SyncAccountAndTransactions(
    supplierRepository: Lazy<SupplierRepository>,
    customerRepository: Lazy<CustomerRepository>,
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
    syncTransactions: Lazy<SyncTransactions>
) {
    private val supplierRepository by lazy { supplierRepository.value }
    private val customerRepository by lazy { customerRepository.value }
    private val getActiveBusinessId by lazy { getActiveBusinessId.value }
    private val syncTransactions by lazy { syncTransactions.value }

    suspend fun execute(accountType: AccountType) {
        val businessId = getActiveBusinessId.execute()
        if (accountType.isSupplier()) {
            supplierRepository.syncSuppliers(businessId = businessId)
        } else {
            customerRepository.syncCustomers(businessId = businessId)
        }
        syncTransactions.execute(businessId = businessId)
    }
}

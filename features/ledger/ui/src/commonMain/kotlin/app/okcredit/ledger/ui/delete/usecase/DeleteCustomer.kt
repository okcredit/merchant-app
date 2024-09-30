package app.okcredit.ledger.ui.delete.usecase

import app.okcredit.ledger.core.CustomerRepository
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class DeleteCustomer constructor(
    customerRepository: Lazy<CustomerRepository>,
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
) {

    private val customerRepository by lazy { customerRepository.value }
    private val getActiveBusinessId by lazy { getActiveBusinessId.value }

    suspend fun execute(customerId: String) {
        val businessId = getActiveBusinessId.execute()
        deleteCustomer(customerId = customerId, businessId = businessId)
    }

    private suspend fun deleteCustomer(
        customerId: String,
        businessId: String,
    ) = customerRepository.deleteCustomer(customerId, businessId)
}

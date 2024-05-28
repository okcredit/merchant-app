package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.core.CustomerRepository
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class SyncCustomers(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val repository: CustomerRepository,
) {

    suspend fun execute(businessId: String? = null) {
        val final = businessId ?: getActiveBusinessId.execute()
        repository.syncCustomers(final)
    }
}
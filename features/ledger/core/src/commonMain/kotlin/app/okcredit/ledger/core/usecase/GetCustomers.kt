package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Customer
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.core.CustomerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetCustomers(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val customerRepository: CustomerRepository,
) {

    fun execute(sortBy: SortBy, limit: Int, offset: Int): Flow<List<Customer>> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(
                customerRepository.listAllCustomers(businessId, sortBy, limit, offset),
            )
        }
    }
}

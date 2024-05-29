package app.okcredit.ledger.core.usecase

import app.okcredit.ledger.contract.model.Supplier
import app.okcredit.ledger.contract.usecase.SortBy
import app.okcredit.ledger.core.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetSuppliers(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val supplierRepository: SupplierRepository,
) {

    fun execute(sortBy: SortBy, limit: Int, offset: Int): Flow<List<Supplier>> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(
                supplierRepository.listAllSuppliers(businessId, sortBy, limit, offset),
            )
        }
    }
}

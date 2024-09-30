package app.okcredit.ledger.ui.delete.usecase

import app.okcredit.ledger.core.SupplierRepository
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class DeleteSupplier constructor(
    supplierRepository: Lazy<SupplierRepository>,
    getActiveBusinessId: Lazy<GetActiveBusinessId>,
) {

    private val supplierRepository by lazy { supplierRepository.value }
    private val getActiveBusinessId by lazy { getActiveBusinessId.value }

    suspend fun execute(supplierId: String) {
        val businessId = getActiveBusinessId.execute()
        supplierRepository.deleteSupplier(supplierId, businessId)
    }
}

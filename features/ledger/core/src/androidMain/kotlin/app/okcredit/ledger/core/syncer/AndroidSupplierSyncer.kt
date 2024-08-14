package app.okcredit.ledger.core.syncer

import app.okcredit.ledger.core.SupplierRepository
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.toStringOrNull
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class AndroidSupplierSyncer(
    private val supplierRepository: SupplierRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
) : SupplierSyncer {

    override suspend fun execute(input: JsonObject) {
        val businessId = input[LedgerSyncManager.BUSINESS_ID]?.toStringOrNull()
            ?: getActiveBusinessId.execute()
        supplierRepository.syncSuppliers(businessId = businessId)
    }

    override fun schedule(input: JsonObject) {
    }
}

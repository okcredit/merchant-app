package app.okcredit.ledger.core.syncer

import app.okcredit.ledger.core.SupplierRepository
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject

@Inject
class AndroidSupplierSyncer(
    private val supplierRepository: SupplierRepository,
) : SupplierSyncer {

    override suspend fun execute(input: JsonObject) {
    }

    override fun schedule(input: JsonObject) {
    }
}

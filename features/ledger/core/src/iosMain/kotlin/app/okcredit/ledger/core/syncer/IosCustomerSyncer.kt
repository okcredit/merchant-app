package app.okcredit.ledger.core.syncer

import app.okcredit.ledger.core.usecase.SyncCustomers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.toStringOrNull

@Inject
class IosCustomerSyncer(
    private val syncCustomers: SyncCustomers,
) : CustomerSyncer {

    override suspend fun execute(input: JsonObject) {
        val businessId = input[LedgerSyncManager.BUSINESS_ID]?.toStringOrNull()
        syncCustomers.execute(businessId = businessId)
    }

    override fun schedule(input: JsonObject) {
        GlobalScope.launch {
            val businessId = input[LedgerSyncManager.BUSINESS_ID]?.toStringOrNull()
            syncCustomers.execute(businessId = businessId)
        }
    }
}

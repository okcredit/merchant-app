package app.okcredit.merchant.usecase

import app.okcredit.ledger.core.syncer.LedgerSyncManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.tatarka.inject.annotations.Inject
import tech.okcredit.ab.AbDataSyncManager
import tech.okcredit.customization.syncer.CustomizationSyncManager
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class HomeDataSyncer(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val abDataSyncManager: AbDataSyncManager,
    private val customizationSyncManager: CustomizationSyncManager,
    private val ledgerSyncManager: LedgerSyncManager,
) {

    suspend fun execute() {
        coroutineScope {
            val businessId = getActiveBusinessId.execute()
            listOf(
                async { abDataSyncManager.scheduleProfileSync(businessId, "HomeDataSyncer") },
                async { customizationSyncManager.scheduleCustomizationSync(businessId) },
                async { ledgerSyncManager.scheduleTransactionSync(businessId, "HomeDataSyncer") },
            ).awaitAll()
        }
    }
}

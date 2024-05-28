package app.okcredit.merchant.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.toJsonObject
import tech.okcredit.ab.AbDataSyncManager
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class HomeDataSyncer(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val abDataSyncManager: AbDataSyncManager,
) {

    suspend fun execute() {
        coroutineScope {
            val businessId = getActiveBusinessId.execute()
            listOf(
                async { abDataSyncManager.scheduleProfileSync(businessId, "HomeDataSyncer") },
            ).awaitAll()
        }
    }
}

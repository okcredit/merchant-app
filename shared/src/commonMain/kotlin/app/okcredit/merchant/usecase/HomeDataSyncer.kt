package app.okcredit.merchant.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.tatarka.inject.annotations.Inject
import tech.okcredit.ab.AbRepository
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class HomeDataSyncer(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val abRepository: AbRepository,
) {

    suspend fun execute() {
        coroutineScope {
            val businessId = getActiveBusinessId.execute()
            listOf(
                async { abRepository.scheduleSync(businessId, "home") },
            ).awaitAll()
        }
    }
}

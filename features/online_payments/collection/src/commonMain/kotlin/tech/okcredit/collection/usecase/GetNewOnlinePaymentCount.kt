package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

class GetNewOnlinePaymentCount @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
) {
    fun execute(): Flow<Int> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(collectionRepository.listOfNewOnlinePaymentsCount(businessId))
        }
    }
}

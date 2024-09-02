package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.CollectionMerchantProfile
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetMerchantCollectionProfile(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val collectionRepository: CollectionRepository,
) {

    fun execute(): Flow<CollectionMerchantProfile> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(collectionRepository.observeBusinessCollectionProfile(businessId))
        }
    }
}

package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository

@Inject
class GetCollectionActivationStatus(private val collectionRepository: CollectionRepository) {

    fun execute(businessId: String): Flow<Boolean> {
        return collectionRepository.isCollectionActivated(businessId)
    }
}

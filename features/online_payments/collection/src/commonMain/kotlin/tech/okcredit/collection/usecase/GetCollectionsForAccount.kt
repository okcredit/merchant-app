package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.OnlinePayment

@Inject
class GetCollectionsForAccount(private val repository: CollectionRepository) {

    fun execute(businessId: String, accountId: String): Flow<List<OnlinePayment>> {
        return repository.observeCollectionsForAccount(
            businessId = businessId,
            accountId = accountId,
        )
    }
}

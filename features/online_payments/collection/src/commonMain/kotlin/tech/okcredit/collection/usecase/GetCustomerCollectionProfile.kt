package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.CollectionCustomerProfile

@Inject
class GetCustomerCollectionProfile(private val repository: CollectionRepository) {

    fun execute(businessId: String, accountId: String): Flow<CollectionCustomerProfile> {
        return repository.getCollectionCustomerProfile(
            businessId = businessId,
            customerId = accountId,
        )
    }
}

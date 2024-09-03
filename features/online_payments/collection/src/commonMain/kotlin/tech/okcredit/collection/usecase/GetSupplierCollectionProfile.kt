package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.CollectionCustomerProfile

class GetSupplierCollectionProfile(private val repository: CollectionRepository) {

    fun execute(businessId: String, accountId: String): Flow<CollectionCustomerProfile> {
        return repository.getSupplierCollectionProfile(
            businessId = businessId,
            accountId = accountId,
        )
    }
}

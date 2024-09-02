package tech.okcredit.collection.usecase

import tech.okcredit.collection.CollectionRepository

class SetPaymentTagViewed(
    private val collectionRepository: CollectionRepository,
) {
    suspend fun execute(businessId: String) {
        collectionRepository.setOnlinePaymentTag(businessId)
    }
}

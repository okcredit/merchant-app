package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import tech.okcredit.collection.local.CollectionLocalSource

class IsCollectionActivatedOrOnlinePaymentExist constructor(
    private val localSource: CollectionLocalSource,
) {
    fun execute(businessId: String): Flow<Boolean> {
        return localSource.getCollectionMerchantProfile(businessId)
            .flatMapLatest { profile ->
                localSource.getOnlinePaymentsCount(businessId)
                    .map {
                        if (profile.payment_address.isNotBlank()) {
                            true
                        } else {
                            it > 0
                        }
                    }
            }
    }
}

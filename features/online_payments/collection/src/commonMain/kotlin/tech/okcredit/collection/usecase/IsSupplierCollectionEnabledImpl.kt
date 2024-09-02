package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.ab.AbRepository
import tech.okcredit.collection.CollectionConstants
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

class IsSupplierCollectionEnabled @Inject constructor(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val ab: AbRepository,
) {

    fun execute(): Flow<Boolean> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(
                combine(
                    ab.isFeatureEnabled(
                        CollectionConstants.FEATURE_SUPPLIER_ONLINE_PAYMENT,
                        businessId = businessId,
                    ),
                    ab.isFeatureEnabled(
                        CollectionConstants.SUPPLIER_COLLECTION,
                        businessId = businessId,
                    ),
                ) { supplierOnlinePaymentEnabled, supplierCollectionEnabled ->
                    supplierOnlinePaymentEnabled || supplierCollectionEnabled
                },
            )
        }
    }
}

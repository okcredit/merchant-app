package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.OnlinePayment
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetAllCollections(
    private val repository: CollectionRepository,
    private val getActiveBusinessId: Lazy<GetActiveBusinessId>,
) {

    fun execute(): Flow<List<OnlinePayment>> {
        return flow {
            val businessId = getActiveBusinessId.value.execute()
            emitAll(repository.observeAllPayments(businessId))
        }
    }
}

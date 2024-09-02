package tech.okcredit.collection.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import tech.okcredit.collection.CollectionRepository
import tech.okcredit.collection.model.KycStatus
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetKycStatus(
    private val collectionRepository: CollectionRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
) {

    fun execute(): Flow<KycStatus> = flow {
        val businessId = getActiveBusinessId.execute()
        emitAll(
            collectionRepository.isCollectionActivated(businessId).flatMapLatest { adopted ->
                if (adopted) {
                    collectionRepository.getKycStatus(businessId).map { if (it.isEmpty()) KycStatus.NOT_SET else KycStatus.valueOf(it) }
                } else {
                    flow { emit(KycStatus.NOT_SET) }
                }
            },
        )
    }
}

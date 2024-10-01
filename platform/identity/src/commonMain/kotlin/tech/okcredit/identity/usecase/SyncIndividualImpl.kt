package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.usecase.SyncIndividual

@Inject
@ContributesBinding(AppScope::class)
class SyncIndividualImpl(
    private val repository: IdentityRepository,
) : SyncIndividual {

    /**
     * Returns pair of Individual ID, List of Business IDs
     */
    override suspend fun execute(flowId: String?): SyncIndividual.Response {
        return repository.getIndividual(flowId ?: randomUUID(), true).map {
            SyncIndividual.Response(it.id, it.businessIds)
        }.first()
    }
}

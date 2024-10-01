package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.contract.model.Business
import tech.okcredit.identity.contract.usecase.GetActiveBusiness
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
@ContributesBinding(AppScope::class)
class GetActiveBusinessImpl(private val localSource: IdentityLocalSource) : GetActiveBusiness {

    override fun execute(): Flow<Business> {
        return localSource.getActiveBusiness()
    }
}

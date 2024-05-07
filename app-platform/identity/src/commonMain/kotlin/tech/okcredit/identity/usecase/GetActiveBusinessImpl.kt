package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.model.Business
import tech.okcredit.identity.contract.usecase.GetActiveBusiness
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
class GetActiveBusinessImpl(private val localSource: IdentityLocalSource) : GetActiveBusiness {

    override fun execute(): Flow<Business> {
        return localSource.getActiveBusiness()
    }
}

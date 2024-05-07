package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.SetActiveBusinessId
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
class SetActiveBusinessIdImpl(private val localSource: IdentityLocalSource) : SetActiveBusinessId {

    override suspend fun execute(businessId: String) {
        localSource.setActiveBusinessId(businessId)
    }
}

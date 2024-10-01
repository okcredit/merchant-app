package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.contract.usecase.SetActiveBusinessId
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
@ContributesBinding(AppScope::class)
class SetActiveBusinessIdImpl(private val localSource: IdentityLocalSource) : SetActiveBusinessId {

    override suspend fun execute(businessId: String) {
        localSource.setActiveBusinessId(businessId)
    }
}

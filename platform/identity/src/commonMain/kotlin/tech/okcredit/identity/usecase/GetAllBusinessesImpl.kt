package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.identity.contract.model.Business
import tech.okcredit.identity.contract.usecase.GetAllBusinesses
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
@ContributesBinding(AppScope::class)
class GetAllBusinessesImpl(
    private val localSource: IdentityLocalSource,
) : GetAllBusinesses {

    override fun execute(): Flow<List<Business>> {
        return localSource.getBusinessList()
    }
}

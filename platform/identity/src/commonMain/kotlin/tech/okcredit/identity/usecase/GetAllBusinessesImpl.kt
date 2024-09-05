package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.model.Business
import tech.okcredit.identity.contract.usecase.GetAllBusinesses
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
class GetAllBusinessesImpl(
    private val localSource: IdentityLocalSource,
) : GetAllBusinesses {

    override fun execute(): Flow<List<Business>> {
        return localSource.getBusinessList()
    }
}

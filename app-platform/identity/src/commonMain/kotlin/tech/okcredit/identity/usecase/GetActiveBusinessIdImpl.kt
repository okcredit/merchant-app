package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.firstOrNull
import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId
import tech.okcredit.identity.local.IdentityLocalSource

@Inject
class GetActiveBusinessIdImpl(private val localSource: IdentityLocalSource) : GetActiveBusinessId {

    override suspend fun execute(): String {
        val businessId = localSource.getActiveBusinessId().firstOrNull() ?: ""
        if (businessId.isEmpty()) {
            localSource.getBusinessList().firstOrNull()?.let {
                localSource.setActiveBusinessId(it.first().id)
                return it.first().id
            }
        }
        return businessId
    }

    override suspend fun thisOrActiveBusinessId(businessId: String?): String {
        return businessId ?: execute()
    }
}

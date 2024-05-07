package tech.okcredit.identity.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.identity.contract.usecase.SetActiveBusinessId

@Inject
class SwitchBusiness constructor(
    private val setActiveBusinessId: SetActiveBusinessId,
) {

    suspend fun execute(businessId: String, businessName: String) {
        setActiveBusinessId(businessId, businessName)
    }

    private suspend fun setActiveBusinessId(businessId: String, businessName: String) {
        setActiveBusinessId.execute(businessId)
    }
}

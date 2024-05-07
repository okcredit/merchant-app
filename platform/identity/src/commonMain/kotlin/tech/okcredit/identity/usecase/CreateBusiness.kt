package tech.okcredit.identity.usecase

import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import okcredit.base.randomUUID
import tech.okcredit.analytics.AnalyticsProvider
import tech.okcredit.identity.IdentityRepository
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class CreateBusiness(
    private val repository: IdentityRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
    private val switchBusiness: SwitchBusiness,
    private val analyticsProvider: AnalyticsProvider,
) {

    companion object {
        const val MAX_BUSINESS_COUNT = 5

        const val BUSINESS_CREATION_RANK = "business_creation_rank"
        const val INDIVIDUAL_ID = "individual_id"
    }

    suspend fun execute(businessName: String, source: String) {
        val businessCount = repository.getBusinessCount()
        if (businessCount == MAX_BUSINESS_COUNT) {
            throw BusinessCountLimitExceededException()
        }

        val activeBusinessId = getActiveBusinessId.execute()
        val newBusiness = repository.createBusiness(businessName, activeBusinessId)
        setAnalyticsProperties(businessCount + 1)
        switchBusiness.execute(newBusiness.id, newBusiness.name)
    }

    private suspend fun setAnalyticsProperties(businessCount: Int) {
        val individualId = repository.getIndividual(flowId = randomUUID()).first().id
        val superProperties = mutableMapOf<String, Any>()
        val userProperties = mutableMapOf<String, Any>()
        userProperties[INDIVIDUAL_ID] = individualId
        superProperties[INDIVIDUAL_ID] = individualId

        userProperties[BUSINESS_CREATION_RANK] = businessCount

        analyticsProvider.setUserProperty(userProperties)
        analyticsProvider.setDeviceProperties(superProperties)
    }
}

class BusinessCountLimitExceededException : Exception("Business count limit exhausted")

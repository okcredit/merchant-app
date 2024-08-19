package app.okcredit.onboarding.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.analytics.AnalyticsProvider
import tech.okcredit.analytics.UserProperties
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.Credential
import tech.okcredit.identity.contract.usecase.SetActiveBusinessId
import tech.okcredit.identity.contract.usecase.SyncIndividual

@Inject
class LoginUser(
    private val authService: AuthService,
    private val syncIndividual: SyncIndividual,
    private val analyticsProvider: AnalyticsProvider,
    private val setActiveBusinessId: SetActiveBusinessId,
) {

    suspend fun execute(credential: Credential, flowId: String, flowType: String): Pair<Boolean, Boolean> {
        val (newUser, appLockEnabled) = authService.authenticate(credential, flowId, flowType)
        val individual = syncIndividual.execute(flowId)
        setupAnalyticsData(
            newUser = newUser,
            individualId = individual.individualId,
            businessId = individual.businessIdList.first()
        )
        setActiveBusinessId.execute(individual.businessIdList.first())
        return newUser to appLockEnabled
    }

    private fun setupAnalyticsData(
        newUser: Boolean,
        individualId: String,
        businessId: String,
    ) {
        analyticsProvider.setIdentity(businessId)

        val properties = mutableMapOf<String, Any>()
        properties[UserProperties.MERCHANT_ID] = businessId
        analyticsProvider.setUserProperty(properties)

        analyticsProvider.setUserProperty(UserProperties.INDIVIDUAL_ID, individualId)
        if (newUser) {
            analyticsProvider.setUserProperty(UserProperties.BUSINESS_CREATION_RANK, "1")
        }
    }
}

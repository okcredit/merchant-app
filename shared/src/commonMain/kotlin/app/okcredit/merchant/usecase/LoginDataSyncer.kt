package app.okcredit.merchant.usecase

import kotlinx.coroutines.flow.firstOrNull
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.toJsonObject
import tech.okcredit.ab.AbRepository
import tech.okcredit.ab.ProfileSyncer
import tech.okcredit.identity.contract.usecase.GetIndividual

@Inject
class LoginDataSyncer(
    private val getIndividual: GetIndividual,
    private val abRepository: AbRepository,
    private val profileSyncer: ProfileSyncer,
) {

    suspend fun execute() {
        val individual = getIndividual.execute().firstOrNull() ?: return
        individual.businessIds.forEach { businessId ->
            syncDataForBusiness(businessId)
        }
    }

    private suspend fun syncDataForBusiness(businessId: String) {
        profileSyncer.execute(mapOf("businessId" to businessId).toJsonObject())
    }
}

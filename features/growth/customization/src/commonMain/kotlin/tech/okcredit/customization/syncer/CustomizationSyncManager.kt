package tech.okcredit.customization.syncer

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toJsonObject

typealias CustomizationSyncer = OneTimeDataSyncer

@Inject
class CustomizationSyncManager(
    private val customizationSyncer: CustomizationSyncer,
) {

    companion object {
        const val BUSINESS_ID = "business_id"
    }

    suspend fun executeCustomizationSync(businessId: String) {
        customizationSyncer.execute(
            mapOf(
                BUSINESS_ID to businessId,
            ).toJsonObject(),
        )
    }

    fun scheduleCustomizationSync(businessId: String) {
        customizationSyncer.schedule(
            mapOf(
                BUSINESS_ID to businessId,
            ).toJsonObject(),
        )
    }
}

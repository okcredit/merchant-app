package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toJsonObject

typealias ProfileSyncer = OneTimeDataSyncer
typealias ExperimentSyncer = OneTimeDataSyncer

@Inject
class AbDataSyncManager(
    private val profileSyncerLazy: Lazy<ProfileSyncer>,
    private val experimentSyncerLazy: Lazy<ExperimentSyncer>,
) {

    private val profileSyncer by lazy { profileSyncerLazy.value }
    private val experimentSyncer by lazy { experimentSyncerLazy.value }

    companion object {
        const val BUSINESS_ID = "business_id"
        const val SOURCE = "source"

        const val EXPERIMENT_NAME = "experiment_name"
        const val EXPERIMENT_VARIANT = "experiment_variant"
        const val EXPERIMENT_STATUS = "experiment_status"
        const val EXPERIMENT_TIME = "experiment_time"
    }

    fun scheduleProfileSync(businessId: String, sourceType: String) {
        profileSyncer.schedule(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to sourceType,
            ).toJsonObject(),
        )
    }

    fun scheduleStartExperiment(
        name: String,
        variant: String,
        status: Int,
        time: Long,
        businessId: String,
    ) {
        experimentSyncer.schedule(
            mapOf(
                EXPERIMENT_NAME to name,
                EXPERIMENT_STATUS to status,
                EXPERIMENT_VARIANT to variant,
                EXPERIMENT_TIME to time,
                BUSINESS_ID to businessId,
            ).toJsonObject(),
        )
    }

    suspend fun executeProfileSync(businessId: String, source: String? = null) {
        profileSyncer.execute(
            mapOf(
                BUSINESS_ID to businessId,
                SOURCE to (source ?: "unknown"),
            ).toJsonObject(),
        )
    }
}

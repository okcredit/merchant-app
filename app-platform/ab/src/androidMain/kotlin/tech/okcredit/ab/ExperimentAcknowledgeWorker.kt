package tech.okcredit.ab

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig
import tech.okcredit.ab.AbDataSyncManager.Companion.BUSINESS_ID
import tech.okcredit.ab.AbDataSyncManager.Companion.EXPERIMENT_NAME
import tech.okcredit.ab.AbDataSyncManager.Companion.EXPERIMENT_STATUS
import tech.okcredit.ab.AbDataSyncManager.Companion.EXPERIMENT_TIME
import tech.okcredit.ab.AbDataSyncManager.Companion.EXPERIMENT_VARIANT

class ExperimentAcknowledgeWorker constructor(
    context: Context,
    params: WorkerParameters,
    private val ab: AbRepositoryImpl,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "ExperimentAcknowledge")) {

    override suspend fun doActualWork() {
        val experimentName = inputData.getString(EXPERIMENT_NAME) ?: ""
        val experimentVariant = inputData.getString(EXPERIMENT_VARIANT) ?: ""
        val experimentStatus = inputData.getInt(EXPERIMENT_STATUS, 0)
        val acknowledgeTime = inputData.getLong(EXPERIMENT_TIME, 0)
        val businessId = inputData.getString(BUSINESS_ID) ?: ""

        ab.acknowledgeExperiment(
            experimentName = experimentName,
            experimentVariant = experimentVariant,
            experimentStatus = experimentStatus,
            acknowledgeTime = acknowledgeTime,
            businessId = businessId,
        )
    }

    @Inject
    class Factory constructor(
        private val ab: AbRepositoryImpl,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return ExperimentAcknowledgeWorker(context, params, ab)
        }
    }
}

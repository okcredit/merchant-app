package tech.okcredit.ab

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toAnyOrNull
import okcredit.base.syncer.toStringOrNull
import java.util.concurrent.TimeUnit

@Inject
class AndroidExperimentSyncer(
    private val workManager: OkcWorkManager,
) : OneTimeDataSyncer {

    companion object {
        const val WORKER_ACKNOWLEDGEMENT = "ab/acknowledgement"
    }

    override suspend fun execute(input: JsonObject) {
    }

    override fun schedule(input: JsonObject) {
        val workName =
            "$WORKER_ACKNOWLEDGEMENT/${input[AbDataSyncManager.EXPERIMENT_NAME]?.toStringOrNull()}"
        val dataBuilder = Data.Builder()
        for (pair in input.entries) {
            dataBuilder.putAll(input.entries.associate { it.key to it.value.toAnyOrNull() })
        }
        val workRequest = OneTimeWorkRequestBuilder<ExperimentAcknowledgeWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .addTag(workName)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .setInputData(dataBuilder.build())
            .build()

        workManager.schedule(
            uniqueWorkName = workName,
            scope = Scope.Individual,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            workRequest = workRequest,
        )
    }
}

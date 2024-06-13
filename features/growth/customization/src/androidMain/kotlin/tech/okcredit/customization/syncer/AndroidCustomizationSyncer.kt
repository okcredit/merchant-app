package tech.okcredit.customization.syncer

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.toStringOrNull
import okcredit.base.syncer.toWorkerData
import tech.okcredit.customization.usecase.SyncCustomizations
import java.util.concurrent.TimeUnit

@Inject
class AndroidCustomizationSyncer(
    private val syncCustomizations: SyncCustomizations,
    private val okcWorkManager: OkcWorkManager,
) : CustomizationSyncer {

    companion object {
        const val WORKER_CUSTOMIZATION = "customization/sync_customizations"
    }

    override suspend fun execute(input: JsonObject) {
        val businessId = input[CustomizationSyncManager.BUSINESS_ID]?.toStringOrNull()
        syncCustomizations.execute(businessId = businessId ?: "")
    }

    override fun schedule(input: JsonObject) {
        val workName = WORKER_CUSTOMIZATION
        val workRequest = OneTimeWorkRequestBuilder<SyncCustomizationsWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .addTag(workName)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
            .setInputData(input.toWorkerData())
            .build()

        okcWorkManager.schedule(
            uniqueWorkName = workName,
            scope = Scope.Individual,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            workRequest = workRequest,
        )
    }
}
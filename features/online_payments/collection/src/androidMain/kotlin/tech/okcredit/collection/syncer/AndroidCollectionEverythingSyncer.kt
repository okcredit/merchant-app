package tech.okcredit.collection.syncer

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toStringOrNull
import tech.okcredit.collection.syncer.CollectionSyncer.Companion.WORKER_TAG_SYNC_EVERYTHING
import java.util.concurrent.TimeUnit

@Inject
class AndroidCollectionEverythingSyncer(
    private val collectionSyncer: Lazy<CollectionSyncer>,
    private val workManager: Lazy<OkcWorkManager>,
) : OneTimeDataSyncer {

    override suspend fun execute(input: JsonObject) {
        val businessId = input[CollectionSyncer.BUSINESS_ID]?.toStringOrNull() ?: return

        collectionSyncer.value.syncAll(
            businessId = businessId,
            source = (input[CollectionSyncer.SOURCE]?.toStringOrNull()) ?: "unknown",
        )
    }

    override fun schedule(input: JsonObject) {
        val businessId = input[CollectionSyncer.BUSINESS_ID]?.toStringOrNull() ?: return

        val workName = WORKER_TAG_SYNC_EVERYTHING

        val workRequest = OneTimeWorkRequestBuilder<SyncMerchantPaymentWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setInputData(
                workDataOf(
                    CollectionSyncer.BUSINESS_ID to businessId,
                    CollectionSyncer.SOURCE to (input[CollectionSyncer.SOURCE]?.toStringOrNull() ?: "unknown"),
                ),
            )
            .addTag(CollectionSyncer.WORKER_TAG_BASE)
            .addTag(workName)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5, TimeUnit.MINUTES)
            .build()

        workManager.value.schedule(
            uniqueWorkName = workName,
            scope = Scope.Business(businessId),
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest = workRequest,
        )
    }
}

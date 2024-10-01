package tech.okcredit.ab

import androidx.work.*
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.toAnyOrNull
import okcredit.base.syncer.toStringOrNull
import java.util.concurrent.TimeUnit

@Inject
class AndroidProfileSyncer(
    private val workManager: () -> OkcWorkManager,
    private val abRepository: () -> AbRepositoryImpl,
) : ProfileSyncer {

    companion object {
        const val PROFILE_SYNCER = "profile_syncer"
    }

    override suspend fun execute(input: JsonObject) {
        val businessId = input[AbDataSyncManager.BUSINESS_ID]?.toStringOrNull() ?: return

        abRepository.invoke().sync(
            businessId = businessId,
            sourceType = (input[AbDataSyncManager.SOURCE]?.toStringOrNull()) ?: "unknown",
        )
    }

    override fun schedule(input: JsonObject) {
        val dataBuilder = Data.Builder()
        for (pair in input.entries) {
            dataBuilder.putAll(input.entries.associate { it.key to it.value.toAnyOrNull() })
        }
        val workRequest = OneTimeWorkRequestBuilder<AbSyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .addTag(PROFILE_SYNCER)
            .setInputData(dataBuilder.build())
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        workManager().schedule(
            uniqueWorkName = PROFILE_SYNCER,
            scope = Scope.Individual,
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest = workRequest,
        )
    }
}

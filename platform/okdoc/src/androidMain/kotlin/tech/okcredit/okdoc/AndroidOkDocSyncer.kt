package tech.okcredit.okdoc

import androidx.work.*
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toAnyOrNull
import java.util.concurrent.TimeUnit

@Inject
class AndroidOkDocSyncer(
    private val workManager: OkcWorkManager,
    private val manager: OkDocUploadManager,
) : OneTimeDataSyncer {

    companion object {
        const val OK_DOC_SYNCER = "ok_doc_syncer"
    }

    override suspend fun execute(input: JsonObject) {
        manager.execute()
    }

    override fun schedule(input: JsonObject) {
        val dataBuilder = Data.Builder()
        for (pair in input.entries) {
            dataBuilder.putAll(input.entries.associate { it.key to it.value.toAnyOrNull() })
        }
        val workRequest = OneTimeWorkRequestBuilder<OkDocWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .addTag(OK_DOC_SYNCER)
            .setInputData(dataBuilder.build())
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .build()

        workManager.schedule(
            uniqueWorkName = OK_DOC_SYNCER,
            scope = Scope.Individual,
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest = workRequest,
        )
    }
}

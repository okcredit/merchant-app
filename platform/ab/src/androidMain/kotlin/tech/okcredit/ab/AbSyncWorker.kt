package tech.okcredit.ab

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig
import tech.okcredit.ab.AbDataSyncManager.Companion.BUSINESS_ID
import tech.okcredit.ab.AbDataSyncManager.Companion.SOURCE

class AbSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val ab: AbRepositoryImpl,
) : BaseCoroutineWorker(context, params, WorkerConfig("ab_sync")) {

    override suspend fun doActualWork() {
        val businessId = inputData.getString(BUSINESS_ID) ?: ""
        val source = inputData.getString(SOURCE) ?: ""

        return ab.sync(businessId, source)
    }

    @Inject
    class Factory(
        private val ab: AbRepositoryImpl,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return AbSyncWorker(context, params, ab)
        }
    }
}

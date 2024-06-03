package tech.okcredit.okdoc

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig

class OkDocWorker(
    context: Context,
    params: WorkerParameters,
    private val manager: OkDocUploadManager,
) : BaseCoroutineWorker(context, params, WorkerConfig("ok_doc")) {

    override suspend fun doActualWork() {
        return manager.execute()
    }

    @Inject
    class Factory(
        private val okDocUploadManager: OkDocUploadManager,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return OkDocWorker(context, params, okDocUploadManager)
        }
    }
}

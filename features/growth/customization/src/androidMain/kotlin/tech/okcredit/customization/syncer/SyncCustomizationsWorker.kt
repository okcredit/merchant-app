package tech.okcredit.customization.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig
import tech.okcredit.customization.usecase.SyncCustomizations

class SyncCustomizationsWorker(
    context: Context,
    params: WorkerParameters,
    private val syncCustomizations: SyncCustomizations,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "SyncCustomizations")) {

    override suspend fun doActualWork() {
        syncCustomizations.execute(
            businessId = inputData.getString(CustomizationSyncManager.BUSINESS_ID) ?: "",
        )
    }

    @Inject
    class Factory(
        private val syncCustomizations: SyncCustomizations,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return SyncCustomizationsWorker(context, params, syncCustomizations)
        }
    }
}

package app.okcredit.ledger.core.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import app.okcredit.ledger.core.usecase.SyncCustomers
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig

class SyncCustomersWorker(
    context: Context,
    params: WorkerParameters,
    private val syncCustomers: SyncCustomers,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "SyncTransactionCommand")) {

    override suspend fun doActualWork() {
        syncCustomers.execute(
            businessId = inputData.getString(LedgerSyncManager.BUSINESS_ID)
        )
    }

    @Inject
    class Factory(
        private val syncCustomers: SyncCustomers,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return SyncCustomersWorker(context, params, syncCustomers)
        }
    }
}
package app.okcredit.ledger.core.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import app.okcredit.ledger.core.usecase.SyncTransactions
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig

class SyncTransactionsWorker(
    context: Context,
    params: WorkerParameters,
    private val syncTransactions: SyncTransactions,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "SyncTransactions")) {

    override suspend fun doActualWork() {
        syncTransactions.execute(
            businessId = inputData.getString(LedgerSyncManager.BUSINESS_ID),
        )
    }

    @Inject
    class Factory(
        private val syncTransactions: SyncTransactions,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return SyncTransactionsWorker(context, params, syncTransactions)
        }
    }
}

package app.okcredit.ledger.core.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import app.okcredit.ledger.core.SupplierRepository
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

class SyncSuppliersWorker(
    context: Context,
    params: WorkerParameters,
    private val supplierRepository: SupplierRepository,
    private val getActiveBusinessId: GetActiveBusinessId,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "SyncSuppliersWorker")) {

    override suspend fun doActualWork() {
        supplierRepository.syncSuppliers(
            businessId = inputData.getString(LedgerSyncManager.BUSINESS_ID) ?: getActiveBusinessId.execute(),
        )
    }

    @Inject
    class Factory(
        private val supplierRepository: SupplierRepository,
        private val getActiveBusinessId: GetActiveBusinessId,
    ) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return SyncSuppliersWorker(
                context = context,
                params = params,
                supplierRepository = supplierRepository,
                getActiveBusinessId = getActiveBusinessId
            )
        }
    }
}

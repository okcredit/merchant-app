package app.okcredit.ledger.core.syncer

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import app.okcredit.ledger.core.usecase.SyncCustomers
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.toStringOrNull
import okcredit.base.syncer.toWorkerData
import java.util.concurrent.TimeUnit

@Inject
class AndroidCustomerSyncer(
    private val syncCustomers: SyncCustomers,
    private val okcWorkManager: OkcWorkManager,
) : CustomerSyncer {

    companion object {
        const val WORKER_CUSTOMER = "ledger/customer"
    }

    override suspend fun execute(input: JsonObject) {
        val businessId = input[LedgerSyncManager.BUSINESS_ID]?.toStringOrNull()
        syncCustomers.execute(businessId = businessId)
    }

    override fun schedule(input: JsonObject) {
        val workName = WORKER_CUSTOMER
        val workRequest = OneTimeWorkRequestBuilder<SyncTransactionsWorker>()
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
            existingWorkPolicy = ExistingWorkPolicy.APPEND_OR_REPLACE,
            workRequest = workRequest,
        )
    }
}

package app.okcredit.ledger.core.syncer

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import app.okcredit.ledger.core.usecase.SyncTransactions
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import okcredit.base.syncer.OkcWorkManager
import okcredit.base.syncer.toAnyOrNull
import okcredit.base.syncer.toStringOrNull
import java.util.concurrent.TimeUnit

@Inject
class AndroidTransactionSyncer(
    private val workManager: OkcWorkManager,
    private val syncTransactions: SyncTransactions,
) : TransactionSyncer {

    companion object {
        const val WORKER_TRANSACTION_COMMAND = "ledger/transaction_command"
    }

    override suspend fun execute(input: JsonObject) {
        val businessId = input["businessId"]?.toStringOrNull()
        syncTransactions.execute(businessId = businessId)
    }

    override fun schedule(input: JsonObject) {
        val workName = WORKER_TRANSACTION_COMMAND
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

        workManager.schedule(
            uniqueWorkName = workName,
            scope = Scope.Individual,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            workRequest = workRequest,
        )
    }
}

private fun JsonObject.toWorkerData(): Data {
    val dataBuilder = Data.Builder()
    for (pair in this.entries) {
        dataBuilder.putAll(this.entries.associate { it.key to it.value.toAnyOrNull() })
    }

    return dataBuilder.build()
}

package tech.okcredit.collection.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.BaseCoroutineWorker
import okcredit.base.syncer.ChildWorkerFactory
import okcredit.base.syncer.WorkerConfig
import tech.okcredit.collection.syncer.CollectionSyncer.Companion.BUSINESS_ID

class SyncMerchantPaymentWorker constructor(
    context: Context,
    params: WorkerParameters,
    private val syncer: Lazy<CollectionSyncer>,
) : BaseCoroutineWorker(context, params, WorkerConfig(label = "SyncMerchantPayment")) {

    override suspend fun doActualWork() {
        val businessId = inputData.getString(BUSINESS_ID)!!
        val source = inputData.getString("SOURCE") ?: "unknown"
        syncer.value.executeSyncMerchantQrPayments(businessId, source)
    }

    @Inject
    class Factory constructor(private val syncer: Lazy<CollectionSyncer>) : ChildWorkerFactory {
        override fun create(context: Context, params: WorkerParameters): ListenableWorker {
            return SyncMerchantPaymentWorker(context, params, syncer)
        }
    }
}

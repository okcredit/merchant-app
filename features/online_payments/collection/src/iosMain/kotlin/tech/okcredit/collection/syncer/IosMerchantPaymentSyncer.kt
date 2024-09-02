package tech.okcredit.collection.syncer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toStringOrNull

@Inject
class IosMerchantPaymentSyncer(
    private val collectionSyncer: () -> CollectionSyncer,
) : OneTimeDataSyncer {

    override suspend fun execute(input: JsonObject) {
        kotlin.runCatching {
            val businessId = input[CollectionSyncer.BUSINESS_ID]?.toStringOrNull() ?: return

            collectionSyncer().executeSyncMerchantQrPayments(
                businessId = businessId,
                source = input[CollectionSyncer.SOURCE]?.toStringOrNull() ?: "unknown",
            )
        }
    }

    override fun schedule(input: JsonObject) {
        GlobalScope.launch {
            execute(input)
        }
    }
}

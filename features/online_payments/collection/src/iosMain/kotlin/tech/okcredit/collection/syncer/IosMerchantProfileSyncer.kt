package tech.okcredit.collection.syncer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toStringOrNull

@Inject
class IosMerchantProfileSyncer(
    private val collectionSyncer: () -> CollectionSyncer,
) : OneTimeDataSyncer {
    override suspend fun execute(input: JsonObject) {
        kotlin.runCatching {
            val businessId = input["businessId"]?.toStringOrNull() ?: return

            collectionSyncer().executeSyncMerchantCollectionProfile(
                businessId = businessId,
                source = input["source"]?.toStringOrNull() ?: "",
            )
        }
    }

    override fun schedule(input: JsonObject) {
        GlobalScope.launch {
            execute(input)
        }
    }
}

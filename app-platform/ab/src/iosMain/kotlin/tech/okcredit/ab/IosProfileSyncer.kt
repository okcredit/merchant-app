package tech.okcredit.ab

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okcredit.base.syncer.toStringOrNull

@Inject
class IosProfileSyncer(
    private val abRepository: () -> AbRepositoryImpl,
) : OneTimeDataSyncer {

    override suspend fun execute(input: JsonObject) {
        kotlin.runCatching {
            abRepository.invoke().sync(
                businessId = input["businessId"]?.toStringOrNull() ?: "",
                sourceType = (input["source"]?.toStringOrNull()) ?: "unknown",
            )
        }
    }

    override fun schedule(input: JsonObject) {
        GlobalScope.launch {
            execute(input)
        }
    }
}

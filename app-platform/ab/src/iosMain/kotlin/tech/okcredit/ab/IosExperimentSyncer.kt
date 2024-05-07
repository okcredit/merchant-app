package tech.okcredit.ab

import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer

@Inject
class IosExperimentSyncer : OneTimeDataSyncer {

    companion object {
        const val WORKER_ACKNOWLEDGEMENT = "ab/acknowledgement"
    }

    override suspend fun execute(input: JsonObject) {
    }

    override fun schedule(input: JsonObject) {
    }
}

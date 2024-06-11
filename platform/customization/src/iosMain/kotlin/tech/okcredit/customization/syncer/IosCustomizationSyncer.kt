package tech.okcredit.customization.syncer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.toStringOrNull
import tech.okcredit.customization.usecase.SyncCustomizations

@Inject
class IosCustomizationSyncer(
    private val syncCustomizations: SyncCustomizations,
) : CustomizationSyncer {

    override suspend fun execute(input: JsonObject) {
        val businessId = input[CustomizationSyncManager.BUSINESS_ID]?.toStringOrNull()
        syncCustomizations.execute(businessId = businessId ?: "")
    }

    override fun schedule(input: JsonObject) {
        GlobalScope.launch {
            execute(input)
        }
    }
}
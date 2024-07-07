package tech.okcredit.customization.usecase

import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.local.CustomizationLocalSource
import tech.okcredit.customization.remote.CustomizationRemoteSource

@Inject
class SyncCustomizations(
    private val remoteSource: CustomizationRemoteSource,
    private val localSource: CustomizationLocalSource,
) {

    suspend fun execute(businessId: String) {
        val customizations = kotlin.runCatching { remoteSource.getCustomizations(businessId) }.getOrNull()
        if (customizations != null) {
            localSource.insertComponents(businessId, customizations)
        }
    }
}

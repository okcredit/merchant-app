package tech.okcredit.customization.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.AppVersionCode
import tech.okcredit.customization.models.TargetComponent

@Inject
class CustomizationRemoteSource(
    private val apiClient: Lazy<CustomizationApiClient>,
    private val appVersionCode: AppVersionCode,
) {

    suspend fun getCustomizations(businessId: String): List<TargetComponent> {
        return apiClient.value.listCustomizations(
            GetCustomizationRequest(
                versionCode = appVersionCode.toString(),
                lang = "en",
            ),
            businessId,
        )
    }
}

package tech.okcredit.customization.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseUrl
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.getOrThrow
import okcredit.base.network.post
import tech.okcredit.customization.models.TargetComponent

@Inject
class CustomizationRemoteSource(
    private val appVersionCode: AppVersionCode,
    private val baseUrl: BaseUrl,
    private val authorizedHttpClient: AuthorizedHttpClient,
) {

    suspend fun getCustomizations(businessId: String): List<TargetComponent> {
        return authorizedHttpClient.post<GetCustomizationRequest, List<TargetComponent>>(
            baseUrl = baseUrl,
            endPoint = "dynamicui/v2/ListCustomizations",
            requestBody = GetCustomizationRequest(
                versionCode = appVersionCode.toString(),
                lang = "en",
            ),
            headers = mapOf(HEADER_BUSINESS_ID to businessId),
        ).getOrThrow()
    }
}

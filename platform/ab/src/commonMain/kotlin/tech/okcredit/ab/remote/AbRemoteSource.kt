package tech.okcredit.ab.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.HEADER_BUSINESS_ID
import okcredit.base.network.get
import okcredit.base.network.getOrNull
import okcredit.base.network.getOrThrow
import okcredit.base.network.post
import tech.okcredit.ab.Profile

@Inject
class AbRemoteSource(
    private val baseUrl: BaseUrl,
    private val authorizedHttpClient: AuthorizedHttpClient,
) {

    suspend fun getProfile(
        deviceId: String,
        sourceType: String,
        businessId: String,
    ): Profile? {
        return authorizedHttpClient.get<GetProfileResponse>(
            baseUrl = baseUrl,
            endPoint = "ab/v2/GetProfile",
            queryParams = mapOf(
                "device_id" to deviceId,
            ),
            headers = mapOf(
                "X-App-Source" to "sync",
                "X-App-Source-Type" to sourceType,
                HEADER_BUSINESS_ID to businessId,
            ),
        ).getOrNull()?.profile?.toProfile()
    }

    suspend fun acknowledgeExperiment(
        deviceId: String,
        experimentName: String,
        experimentVariant: String,
        experimentStatus: Int,
        acknowledgeTime: Long,
        businessId: String,
    ) {
        authorizedHttpClient.post<AcknowledgementRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "ab/v2/Ack",
            requestBody = AcknowledgementRequest(
                deviceId = deviceId,
                type = experimentStatus,
                time = acknowledgeTime,
                experiments = listOf(Experiment(experimentName, 0, experimentVariant, mapOf())),
            ),
            headers = mapOf(
                "X-App-Source" to "sync",
                "X-App-Source-Type" to "user_action",
                HEADER_BUSINESS_ID to businessId,
            ),
        )
    }

    suspend fun disableFeature(
        feature: String,
        businessId: String,
    ) {
        authorizedHttpClient.post<DisableFeatureRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "ab/v1/DisableFeature",
            requestBody = DisableFeatureRequest(
                feature = feature,
                merchantIds = listOf(businessId),
                reason = "user_action",
            ),
            headers = mapOf(
                "X-App-Source" to "disable_feature",
                "X-App-Source-Type" to "user_action",
            ),
        )
    }

    suspend fun enableFeature(feature: String, businessId: String) {
        authorizedHttpClient.post<EnableFeatureRequest, Unit>(
            baseUrl = baseUrl,
            endPoint = "ab/v1/EnableFeature",
            requestBody = EnableFeatureRequest(
                feature = feature,
                merchantIds = listOf(businessId),
            ),
            headers = mapOf(
                "X-App-Source" to "enable_feature",
                "X-App-Source-Type" to "user_action",
            ),
        )
    }
}

internal fun tech.okcredit.ab.remote.Profile.toProfile(): Profile =
    Profile(features = features, experiments = experiments)

package tech.okcredit.ab.remote

import me.tatarka.inject.annotations.Inject
import okcredit.base.network.asError
import tech.okcredit.ab.Profile

@Inject
class AbRemoteSource(
    private val abApiClient: Lazy<AbApiClient>,
) {

    suspend fun getProfile(
        deviceId: String,
        sourceType: String,
        businessId: String,
    ): Profile {
        val response = abApiClient.value.getProfile(
            deviceId = deviceId,
            source = "sync",
            sourceType = sourceType,
            businessId = businessId,
        )
        return if (response.isSuccessful) {
            response.body()?.profile?.toProfile() ?: Profile(hashMapOf())
        } else {
            throw response.asError()
        }
    }

    suspend fun acknowledgeExperiment(
        deviceId: String,
        experimentName: String,
        experimentVariant: String,
        experimentStatus: Int,
        acknowledgeTime: Long,
        businessId: String,
    ) {
        abApiClient.value.acknowledge(
            req = AcknowledgementRequest(
                device_id = deviceId,
                type = experimentStatus,
                time = acknowledgeTime,
                experiments = listOf(Experiment(experimentName, 0, experimentVariant, mapOf())),
            ),
            source = "sync",
            sourceType = "user_action",
            businessId = businessId,
        )
    }

    suspend fun disableFeature(
        feature: String,
        businessId: String,
    ) {
        abApiClient.value.disableFeature(
            req = DisableFeatureRequest(
                feature = feature,
                merchantIds = listOf(businessId),
                reason = "user_action",
            ),
            source = "disable_feature",
            sourceType = "user_action",
        )
    }

    suspend fun enableFeature(feature: String, businessId: String) {
        abApiClient.value.enableFeature(
            req = EnableFeatureRequest(
                feature = feature,
                merchantIds = listOf(businessId),
            ),
            source = "enable_feature",
            sourceType = "user_action",
        )
    }
}

internal fun tech.okcredit.ab.remote.Profile.toProfile(): Profile =
    Profile(features = features, experiments = experiments)

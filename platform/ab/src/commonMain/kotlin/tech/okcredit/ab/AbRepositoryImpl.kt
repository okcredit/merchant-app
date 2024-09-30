package tech.okcredit.ab

import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Singleton
import okcredit.base.network.ApiError
import okcredit.base.network.DeviceIdProvider
import tech.okcredit.ab.local.AbLocalSource
import tech.okcredit.ab.remote.AbRemoteSource

@Inject
@Singleton
class AbRepositoryImpl(
    private val localSourceLazy: Lazy<AbLocalSource>,
    private val remoteSourceLazy: Lazy<AbRemoteSource>,
    private val deviceIdProviderLazy: Lazy<DeviceIdProvider>,
    private val abDataSyncManagerLazy: Lazy<AbDataSyncManager>,
) : AbRepository {

    private val localSource by lazy { localSourceLazy.value }
    private val remoteSource by lazy { remoteSourceLazy.value }
    private val deviceIdProvider by lazy { deviceIdProviderLazy.value }
    private val abDataSyncManager by lazy { abDataSyncManagerLazy.value }

    override suspend fun clearLocalData() = localSource.clearAll()

    override fun isFeatureEnabled(
        feature: String,
        ignoreCache: Boolean,
        businessId: String,
    ): Flow<Boolean> =
        localSource.isFeatureEnabled(feature, businessId).distinctUntilChanged()

    override fun enabledFeatures(businessId: String): Flow<List<String>> =
        localSource.enabledFeatures(businessId)

    override fun isExperimentEnabled(experiment: String, businessId: String): Flow<Boolean> {
        return localSource.isExperimentEnabled(experiment = experiment, businessId = businessId)
    }

    override fun getExperimentVariant(name: String, businessId: String): Flow<String> =
        localSource.getExperimentVariant(name, businessId).onEach {
            if (it.isEmpty().not()) {
                startExperiment(name, businessId)
            }
        }

    override fun getVariantConfigurations(
        name: String,
        businessId: String,
    ): Flow<Map<String, String>> =
        localSource.getVariantConfigurations(name, businessId)

    private suspend fun startExperiment(name: String, businessId: String) {
        val startedExperiments = localSource.startedExperiments(businessId).first()
        val variant = localSource.getExperimentVariant(name, businessId).first()
        if (startedExperiments.contains(name).not()) {
            localSource.recordExperimentStarted(name, businessId)
            scheduleStartExperiment(
                name = name,
                variant = variant,
                status = ExperimentAckStates.STARTED.value,
                time = Clock.System.now().toEpochMilliseconds(),
                businessId = businessId,
            )
        }
    }

    suspend fun sync(businessId: String, sourceType: String) {
        try {
            val profile = remoteSource.getProfile(
                deviceId = deviceIdProvider.current(),
                sourceType = sourceType,
                businessId = businessId,
            )
            if (profile != null) {
                localSource.setProfile(profile, businessId)
            }
        } catch (apiError: ApiError) {
            apiError.printStackTrace()
        }
    }

    private fun scheduleStartExperiment(
        name: String,
        variant: String,
        status: Int,
        time: Long,
        businessId: String,
    ) {
        abDataSyncManager.scheduleStartExperiment(
            name = name,
            variant = variant,
            status = status,
            time = time,
            businessId = businessId,
        )
    }

    override suspend fun disableFeature(vararg feature: String, businessId: String) {
        feature.forEach {
            remoteSource.disableFeature(it, businessId)
        }
        sync(businessId, "disable_feature")
    }

    override suspend fun enableFeature(vararg feature: String, businessId: String) {
        feature.forEach {
            remoteSource.enableFeature(it, businessId)
        }
        sync(businessId, "enable_feature")
    }

    suspend fun acknowledgeExperiment(
        experimentName: String,
        experimentVariant: String,
        experimentStatus: Int,
        acknowledgeTime: Long,
        businessId: String,
    ) {
        remoteSource.acknowledgeExperiment(
            deviceId = deviceIdProvider.current(),
            experimentName = experimentName,
            experimentVariant = experimentVariant,
            experimentStatus = experimentStatus,
            acknowledgeTime = acknowledgeTime,
            businessId = businessId,
        )
    }
}

enum class ExperimentAckStates(val value: Int) {
    STARTED(0),
}

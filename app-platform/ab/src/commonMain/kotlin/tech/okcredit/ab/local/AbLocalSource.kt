package tech.okcredit.ab.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.ab.Profile
import tech.okcredit.ab.remote.Experiment

@Inject
class AbLocalSource constructor(
    private val queries: AbDatabaseQueries,
    private val ioDispatcher: IoDispatcher,
) {

    fun isFeatureEnabled(feature: String, businessId: String): Flow<Boolean> {
        return queries.isFeatureEnabled(feature, businessId).asFlow().mapToOne(ioDispatcher)
            .catch { emit(false) }
    }

    fun isExperimentEnabled(experiment: String, businessId: String): Flow<Boolean> {
        return queries.isExperimentEnabled(experiment, businessId).asFlow().mapToOne(ioDispatcher)
            .catch { emit(false) }
    }

    fun getExperimentVariant(name: String, businessId: String): Flow<String> {
        return queries.getExperimentVariant(name, businessId).asFlow().mapToOne(ioDispatcher)
    }

    fun enabledFeatures(businessId: String): Flow<List<String>> {
        return queries.enabledFeatures(businessId).asFlow().mapToList(ioDispatcher)
    }

    fun getVariantConfigurations(name: String, businessId: String): Flow<Map<String, String>> {
        return queries.getVariantConfigurations(name, businessId).asFlow().mapToOne(ioDispatcher)
            .map {
                Json.decodeFromString(it)
            }
    }

    fun getProfile(businessId: String): Flow<Profile> {
        return combine(
            queries.getAllFeatures(businessId).asFlow().mapToList(ioDispatcher),
            queries.getAllExperiments(businessId).asFlow().mapToList(ioDispatcher),
        ) { features, experiments ->
            val featuresMap = mutableMapOf<String, Boolean>()
            features.forEach {
                featuresMap[it.name] = it.enabled
            }
            val experimentsMap = mutableMapOf<String, Experiment>()
            experiments.forEach {
                experimentsMap[it.name] = Experiment(
                    name = it.name,
                    status = 0,
                    variant = it.variant,
                    vars = Json.decodeFromString(it.vars),
                )
            }
            Profile(
                features = featuresMap,
                experiments = experimentsMap,
            )
        }
    }

    fun startedExperiments(businessId: String): Flow<List<String>> {
        return queries.startedExperiments(businessId).asFlow().mapToList(ioDispatcher)
    }

    suspend fun recordExperimentStarted(name: String, businessId: String) {
        withContext(ioDispatcher) {
            queries.recordExperimentStarted(name, businessId)
        }
    }

    suspend fun setProfile(profile: Profile, businessId: String) {
        withContext(ioDispatcher) {
            profile.features.forEach {
                queries.insertFeatures(
                    id = null,
                    name = it.key,
                    enabled = it.value,
                    businessId = businessId,
                )
            }
            profile.experiments.forEach {
                queries.insertExperiments(
                    id = null,
                    name = it.key,
                    variant = it.value.variant,
                    vars = Json.encodeToString(it.value.vars),
                    started = false,
                    businessId = businessId,
                )
            }
        }
    }

    suspend fun clearAll() {
        withContext(ioDispatcher) {
            queries.deleteAllExperiments()
            queries.deleteAllFeatures()
        }
    }
}

package tech.okcredit.ab

import kotlinx.coroutines.flow.Flow

interface AbRepository {

    fun isFeatureEnabled(feature: String, ignoreCache: Boolean = false, businessId: String): Flow<Boolean>

    fun isExperimentEnabled(experiment: String, businessId: String): Flow<Boolean>

    fun getExperimentVariant(name: String, businessId: String): Flow<String>

    suspend fun clearLocalData()

    fun getVariantConfigurations(name: String, businessId: String): Flow<Map<String, String>>

    suspend fun disableFeature(vararg feature: String, businessId: String)

    suspend fun enableFeature(vararg feature: String, businessId: String)

    fun enabledFeatures(businessId: String): Flow<List<String>>
}

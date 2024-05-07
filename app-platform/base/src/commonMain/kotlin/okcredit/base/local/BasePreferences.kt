@file:OptIn(ExperimentalSettingsApi::class)

package okcredit.base.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okcredit.base.appDispatchers
import okcredit.base.local.Scope.Companion.getScopedKey

open class BasePreferences constructor(
    private val prefs: Lazy<FlowSettings>,
    private val coroutineDispatcher: CoroutineDispatcher = appDispatchers.io,
) {

    fun getString(key: String, scope: Scope, defaultValue: String = ""): Flow<String> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(prefs.value.getStringFlow(scopedKey, defaultValue))
        }.flowOn(coroutineDispatcher)
    }

    fun getBoolean(key: String, scope: Scope, defaultValue: Boolean = false): Flow<Boolean> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(prefs.value.getBooleanFlow(scopedKey, defaultValue))
        }.flowOn(coroutineDispatcher)
    }

    fun getInt(key: String, scope: Scope, defaultValue: Int = 0): Flow<Int> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(prefs.value.getIntFlow(scopedKey, defaultValue))
        }.flowOn(coroutineDispatcher)
    }

    fun getLong(key: String, scope: Scope, defaultValue: Long = 0L): Flow<Long> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(prefs.value.getLongFlow(scopedKey, defaultValue))
        }.flowOn(coroutineDispatcher)
    }

    fun getFloat(key: String, scope: Scope, defaultValue: Float = 0f): Flow<Float> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(prefs.value.getFloatFlow(scopedKey, defaultValue))
        }.flowOn(coroutineDispatcher)
    }

    fun <T : Any> getObject(
        key: String,
        scope: Scope,
        defaultValue: T?,
        converter: Converter<T>,
    ): Flow<T?> {
        return flow {
            val scopedKey = getScopedKey(key, scope)
            emitAll(
                prefs.value.getStringFlow(
                    key = scopedKey,
                    defaultValue = defaultValue?.let { converter.serialize(it) } ?: "",
                ).map {
                    if (it.isEmpty()) {
                        defaultValue
                    } else {
                        converter.deserialize(it)
                    }
                },
            )
        }.flowOn(coroutineDispatcher)
    }

    //endregion

    //region Setters

    suspend fun set(key: String, value: String, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.putString(scopedKey, value)
    }

    suspend fun set(key: String, value: Boolean, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.putBoolean(scopedKey, value)
    }

    suspend fun set(key: String, value: Int, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.putInt(scopedKey, value)
    }

    suspend fun set(key: String, value: Long, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.putLong(scopedKey, value)
    }

    suspend fun set(key: String, value: Float, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.putFloat(scopedKey, value)
    }

    suspend fun <T : Any> set(key: String, value: T, scope: Scope, converter: Converter<T>) =
        withContext(coroutineDispatcher) {
            val scopedKey = getScopedKey(key, scope)
            prefs.value.putString(scopedKey, converter.serialize(value))
        }

    //endregion

    suspend fun contains(key: String, scope: Scope): Boolean = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        return@withContext prefs.value.hasKey(scopedKey)
    }

    suspend fun remove(key: String, scope: Scope) = withContext(coroutineDispatcher) {
        val scopedKey = getScopedKey(key, scope)
        prefs.value.remove(scopedKey)
    }

    // preserve shared preference version and clear data
    @OptIn(ExperimentalSettingsApi::class)
    suspend fun clear() = withContext(coroutineDispatcher) {
        prefs.value.clear()
    }
}

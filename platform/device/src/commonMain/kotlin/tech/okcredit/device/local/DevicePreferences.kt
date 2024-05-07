package tech.okcredit.device.local

import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import okcredit.base.local.BasePreferences
import okcredit.base.local.Converter
import okcredit.base.local.Scope
import tech.okcredit.device.DeviceSettingsFactory

@OptIn(ExperimentalSettingsApi::class)
@Inject
class DevicePreferences constructor(
    settingsFactory: DeviceSettingsFactory,
    ioDispatcher: IoDispatcher,
) : BasePreferences(lazy { settingsFactory.create() }, ioDispatcher) {

    private val deviceConverter = object : Converter<DbDevice> {
        override fun deserialize(serialized: String): DbDevice {
            return Json.decodeFromString(serialized)
        }

        override fun serialize(value: DbDevice): String {
            return Json.encodeToString(value)
        }
    }

    suspend fun getDevice(): DbDevice? {
        return getObject(PREFS_DEVICE, Scope.Individual, null, deviceConverter).firstOrNull()
    }

    suspend fun putDevice(dbDevice: DbDevice) {
        set(PREFS_DEVICE, dbDevice, Scope.Individual, deviceConverter)
    }

    suspend fun getIpRegion(): String? {
        val device = getObject(PREFS_DEVICE, Scope.Individual, null, deviceConverter).firstOrNull()
        return device?.ipRegion
    }

    suspend fun setIpRegion(value: String) {
        val device = getObject(PREFS_DEVICE, Scope.Individual, null, deviceConverter).firstOrNull()
        device?.let {
            putDevice(it.copy(ipRegion = value))
        }
    }

    companion object {
        const val PREFS_DEVICE = "prefs_device"
    }
}

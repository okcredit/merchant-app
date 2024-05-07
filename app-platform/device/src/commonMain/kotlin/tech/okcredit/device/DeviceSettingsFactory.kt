package tech.okcredit.device

import com.russhwolf.settings.coroutines.FlowSettings

interface DeviceSettingsFactory {
    fun create(): FlowSettings
}

const val PREF_NAME = "device_prefs"

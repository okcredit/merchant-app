package tech.okcredit.auth

import com.russhwolf.settings.coroutines.FlowSettings

interface SettingsFactory {
    fun create(): FlowSettings
}

const val PREF_NAME = "auth-prefs"

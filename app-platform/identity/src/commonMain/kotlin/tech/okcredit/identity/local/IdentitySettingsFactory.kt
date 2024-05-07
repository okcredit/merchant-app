package tech.okcredit.identity.local

import com.russhwolf.settings.coroutines.FlowSettings

interface IdentitySettingsFactory {
    fun create(): FlowSettings
}

const val PREF_NAME = "identity_prefs"

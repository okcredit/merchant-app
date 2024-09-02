package tech.okcredit.collection.local

import com.russhwolf.settings.coroutines.FlowSettings

interface CollectionSettingsFactory {
    fun create(): FlowSettings
}

const val PREF_NAME = "collection_prefs"

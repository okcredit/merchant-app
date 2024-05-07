package tech.okcredit.auth

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher

@Inject
class DesktopAuthSettingsFactory(private val ioDispatcher: IoDispatcher) : SettingsFactory {

    override fun create(): FlowSettings {
        return PreferencesSettings.Factory().create(PREF_NAME).toFlowSettings(ioDispatcher)
    }
}

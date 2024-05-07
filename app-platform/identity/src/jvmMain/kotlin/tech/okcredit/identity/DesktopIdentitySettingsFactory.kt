package tech.okcredit.identity

import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.identity.local.IdentitySettingsFactory
import tech.okcredit.identity.local.PREF_NAME

@Inject
class DesktopIdentitySettingsFactory(private val ioDispatcher: IoDispatcher) : IdentitySettingsFactory {

    override fun create(): FlowSettings {
        return PreferencesSettings.Factory().create(PREF_NAME).toFlowSettings(ioDispatcher)
    }
}

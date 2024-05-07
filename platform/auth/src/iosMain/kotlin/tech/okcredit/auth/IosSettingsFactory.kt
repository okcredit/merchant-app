package tech.okcredit.auth

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher

@Inject
class IosSettingsFactory(private val ioDispatcher: IoDispatcher) : SettingsFactory {

    override fun create(): FlowSettings {
        return NSUserDefaultsSettings.Factory().create().toFlowSettings(ioDispatcher)
    }
}

package tech.okcredit.device

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher

@Inject
class IosDeviceSettingsFactory(private val ioDispatcher: IoDispatcher) : DeviceSettingsFactory {

    override fun create(): FlowSettings {
        return NSUserDefaultsSettings.Factory().create(PREF_NAME).toFlowSettings(ioDispatcher)
    }
}

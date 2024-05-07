package tech.okcredit.device

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher

@Inject
class AndroidDeviceSettingsFactory(private val context: Context, private val ioDispatcher: IoDispatcher) :
    DeviceSettingsFactory {

    override fun create(): FlowSettings {
        return SharedPreferencesSettings.Factory(context).create(PREF_NAME)
            .toFlowSettings(ioDispatcher)
    }
}

package tech.okcredit.identity

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.identity.local.IdentitySettingsFactory
import tech.okcredit.identity.local.PREF_NAME

@Inject
class AndroidIdentitySettingsFactory(private val context: Context, private val ioDispatcher: IoDispatcher) :
    IdentitySettingsFactory {

    override fun create(): FlowSettings {
        return SharedPreferencesSettings.Factory(context).create(PREF_NAME)
            .toFlowSettings(ioDispatcher)
    }
}

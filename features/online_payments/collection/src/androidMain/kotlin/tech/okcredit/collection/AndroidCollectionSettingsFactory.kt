package tech.okcredit.collection

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.collection.local.CollectionSettingsFactory
import tech.okcredit.collection.local.PREF_NAME

@Inject
class AndroidCollectionSettingsFactory(
    private val context: Context,
    private val ioDispatcher: IoDispatcher,
) : CollectionSettingsFactory {

    override fun create(): FlowSettings {
        return SharedPreferencesSettings.Factory(context).create(PREF_NAME)
            .toFlowSettings(ioDispatcher)
    }
}

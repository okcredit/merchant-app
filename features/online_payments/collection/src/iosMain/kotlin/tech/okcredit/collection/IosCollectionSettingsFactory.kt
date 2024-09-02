package tech.okcredit.collection

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher
import tech.okcredit.collection.local.CollectionSettingsFactory
import tech.okcredit.collection.local.PREF_NAME

@Inject
class IosCollectionSettingsFactory(private val ioDispatcher: IoDispatcher) : CollectionSettingsFactory {

    override fun create(): FlowSettings {
        return NSUserDefaultsSettings.Factory().create(PREF_NAME).toFlowSettings(ioDispatcher)
    }
}
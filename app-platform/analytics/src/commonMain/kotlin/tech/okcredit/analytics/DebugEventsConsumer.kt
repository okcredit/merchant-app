package tech.okcredit.analytics

import co.touchlab.kermit.Logger
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.Debug
import okcredit.base.di.Singleton

@Singleton
@Inject
class DebugEventsConsumer(val debug: Debug) : AnalyticEventsConsumer {

    override fun setIdentity(id: String) {
        if (!debug) return

        Logger.d("[Analytics] setIdentity: $id")
    }

    override fun setUserProperty(properties: Map<String, Any>) {
        if (!debug) return

        Logger.d("[Analytics] setUserProperty: $properties")
    }

    override fun setDeviceProperty(properties: Map<String, Any>) {
        if (!debug) return

        Logger.d("[Analytics] setSuperProperties: $properties")
    }

    override fun trackEvent(eventName: String, properties: Map<String, Any>?) {
        if (!debug) return

        if (properties != null) {
            Logger.d("[Analytics] $eventName: $properties")
        } else {
            Logger.d("[Analytics] $eventName")
        }
    }

    override val metered: Boolean
        get() = false
}

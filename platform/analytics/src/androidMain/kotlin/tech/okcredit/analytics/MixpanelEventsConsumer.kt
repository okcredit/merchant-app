package tech.okcredit.analytics

import com.mixpanel.android.mpmetrics.MixpanelAPI
import me.tatarka.inject.annotations.Inject
import org.json.JSONException
import org.json.JSONObject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class MixpanelEventsConsumer(
    private val mixpanel: MixpanelAPI,
) : AnalyticEventsConsumer {

    override fun setIdentity(id: String) {
        mixpanel.identify(id)
    }

    override fun setUserProperty(properties: Map<String, Any>) {
        for ((key, value) in properties) {
            mixpanel.people.set(handleStandardProps(key), value)
        }
    }

    override fun setDeviceProperty(properties: Map<String, Any>) {
        try {
            val props = JSONObject()
            for ((key, value) in properties) {
                props.put(key, value)
            }
            mixpanel.registerSuperProperties(props)
        } catch (e: JSONException) {
        }
    }

    override fun trackEvent(eventName: String, properties: Map<String, Any>?) {
        if (properties == null) {
            mixpanel.track(eventName)
        } else {
            val props = JSONObject()
            for ((key, value) in properties) {
                props.put(key, value)
            }
            mixpanel.track(eventName, props)
        }
    }

    override val metered: Boolean
        get() = true

    private fun handleStandardProps(key: String): String {
        return when (key) {
            "name" -> "\$name"
            "phone" -> "\$phone"
            else -> key
        }
    }
}

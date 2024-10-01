package tech.okcredit.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, multibinding = true)
class FirebaseEventsConsumer(
    private val context: Context,
) : AnalyticEventsConsumer {

    private val firebase by lazy { FirebaseAnalytics.getInstance(context) }

    override fun setIdentity(id: String) {
        firebase.setUserId(id)
        firebase.setUserProperty(MERCHANT_ID, id)
    }

    override fun setUserProperty(properties: Map<String, Any>) {
        for ((key, value) in properties) {
            firebase.setUserProperty(
                formatKey(key, MAX_USER_PROPERTY_NAME_LENGTH).lowercase(),
                value.toString(),
            )
        }
    }

    override fun trackEvent(eventName: String, properties: Map<String, Any>?) {
        val formattedEventName = formatKey(eventName, MAX_EVENT_NAME_LENGTH)
        if (properties == null) {
            val params = Bundle()
            firebase.logEvent(formattedEventName, params)
        } else {
            val bundle = Bundle()
            for ((key, value) in properties) {
                val formattedKey = formatKey(key, MAX_EVENT_PARAMETER_NAME_LENGTH)
                when (value) {
                    is Int -> {
                        bundle.putInt(formattedKey, value)
                    }
                    is Long -> {
                        bundle.putLong(formattedKey, value)
                    }
                    is Double -> {
                        bundle.putDouble(formattedKey, value)
                    }
                    is Boolean -> {
                        bundle.putBoolean(formattedKey, value)
                    }
                    is Float -> {
                        bundle.putFloat(formattedKey, value)
                    }
                    else -> {
                        bundle.putString(formattedKey, value.toString())
                    }
                }
            }
            firebase.logEvent(formattedEventName, bundle)
        }
    }

    override val metered: Boolean
        get() = false

    private fun formatKey(key: String, maxLength: Int): String {
        var newKey = key.replace(" ", "_")
        newKey = newKey.replace("[(-+.^:,)]".toRegex(), "_")
        while (newKey.startsWith("_")) {
            newKey = newKey.substring(1)
        }

        if (newKey.length > maxLength) {
            newKey = newKey.substring(0, maxLength - 1)
        }

        return newKey
    }

    companion object {
        // Check https://support.google.com/firebase/answer/9237506?hl=en for latest details
        private const val MAX_EVENT_NAME_LENGTH = 40
        private const val MAX_EVENT_PARAMETER_NAME_LENGTH = 40
        private const val MAX_USER_PROPERTY_NAME_LENGTH = 24

        const val MERCHANT_ID = "merchant_id"
    }
}

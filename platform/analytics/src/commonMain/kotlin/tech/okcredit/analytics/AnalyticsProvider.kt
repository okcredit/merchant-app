package tech.okcredit.analytics

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import tech.okcredit.analytics.AnalyticsProvider.EventChannel.*

@Inject
@SingleIn(AppScope::class)
class AnalyticsProvider(consumers: Set<AnalyticEventsConsumer>) {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    /**
     * Problem:
     * Sequentially fired events are not guaranteed to be logged in the same sequence. This happens due to use of launch
     *
     * eg : Code that fires events in order of [1,2,3] might have events logged as [2,1,3].
     * We recently saw this while testing SyncCustomerCommands engineering events. This would inevitably break funnels
     * due to events lost to bad ordering
     *
     * Solution
     * Use locks and FIFOs (channel) to ensure that events are sequentially consumed/logged
     */
    private val fifo = Channel<AnalyticsLoggingRequest>(capacity = Channel.UNLIMITED)
    private val readLock = Mutex()

    private val meteredConsumers = consumers.filter { it.metered }.toSet()
    private val unmeteredConsumers = consumers.filter { !it.metered }.toSet()

    /**
     * Adds support for sampling of events by device identifier. This is necessary as Metered channels cost a lot to
     * maintain in the form of Saas fees. Sampling allows us to keep funnel ratios while reducing these costs.
     *
     * We set this as a part of Super / Device property so that it is reported to those supporting device properties
     */
    private val isSampled = flowOf(false) // todo replace with actual implementation
        .onStart { emit(false) }
        .onEach { setDeviceProperties(mapOf(IS_SAMPLED_FOR_METERED_CHANNEL to it)) }
        .stateIn(scope, SharingStarted.Eagerly, false)

    fun setIdentity(id: String) {
        meteredConsumers.forEach { it.setIdentity(id) }
        unmeteredConsumers.forEach { it.setIdentity(id) }
    }

    fun setUserProperty(properties: Map<String, Any>) {
        meteredConsumers.forEach { it.setUserProperty(properties) }
        unmeteredConsumers.forEach { it.setUserProperty(properties) }
    }

    fun setUserProperty(key: String, value: Any) {
        meteredConsumers.forEach { it.setUserProperty(mapOf(key to value)) }
        unmeteredConsumers.forEach { it.setUserProperty(mapOf(key to value)) }
    }

    fun setDeviceProperties(properties: Map<String, Any>) {
        meteredConsumers.forEach { it.setDeviceProperty(properties) }
        unmeteredConsumers.forEach { it.setDeviceProperty(properties) }
    }

    /**
     * Product requirement is to have the ability to programmatically decide on backend if a user is to be included in
     * sampling or not.
     *
     * In order to do that it is necessary to compute sampling check in sync with AB features set. If AB feature is not
     * available then consider the user to not be sampled. Setting Device/Super property to track this.
     *
     * Amongst the explored solutions, using an enum/bitmask to decide which channel to push the event to and then
     * checking for sampling before pushing is the most straight forward.
     */
    private enum class EventChannel {
        ALL_CHANNELS,
        ENGINEERING_CHANNELS,
        SAMPLED_COMPREHENSIVE_CHANNELS,
    }

    /**
     * Log event to unmetered channels only
     */
    fun logEngineeringEvent(eventName: String, properties: Map<String, Any>?) {
        val propertiesMap = mutableMapOf<String, Any>(LOG_EVENT_CHANNEL_NAME to "EngineeringEvent") +
            (properties ?: emptyMap())
        trackEvent(eventName, propertiesMap, ENGINEERING_CHANNELS)
    }

    /**
     * Log event to unmetered + metered channels
     */
    fun logProductEvent(eventName: String, properties: Map<String, Any>?) {
        val propertiesMap = mutableMapOf<String, Any>(LOG_EVENT_CHANNEL_NAME to "ProductEvent") +
            (properties ?: emptyMap())
        trackEvent(eventName, propertiesMap, ALL_CHANNELS)
    }

    /**
     * Log event to unmetered + metered channels
     */
    fun logErrorEvent(eventName: String, properties: Map<String, Any>?) {
        val propertiesMap = mutableMapOf<String, Any>(LOG_EVENT_CHANNEL_NAME to "ErrorEvent") +
            (properties ?: emptyMap())
        trackEvent(eventName, propertiesMap, ALL_CHANNELS)
    }

    /**
     * Log event to unmetered + sampled metered channels
     */
    fun logHealthMetricEvent(eventName: String, properties: Map<String, Any>?) {
        val propertiesMap = mutableMapOf<String, Any>(LOG_EVENT_CHANNEL_NAME to "HealthMetricEvent") +
            (properties ?: emptyMap())
        trackEvent(eventName, propertiesMap, SAMPLED_COMPREHENSIVE_CHANNELS)
    }

    private fun trackEvent(
        eventName: String,
        properties: Map<String, Any>?,
        channel: EventChannel,
    ) {
        fifo.trySend(AnalyticsLoggingRequest(eventName, properties, channel))
        scope.launch { consumePendingEvents() }
    }

    private suspend fun consumePendingEvents() {
        readLock.withLock {
            while (true) {
                fifo.tryReceive().getOrNull()?.apply {
                    val providers = channel.toConsumers(isSampled.value)
                    runCatching { providers.forEach { it.trackEvent(eventName, properties) } }
                        .exceptionOrNull()
                        ?.also { Logger.e(it) { "Exception while consuming event" } }
                } ?: break
            }
        }
    }

    private fun EventChannel.toConsumers(isSampled: Boolean): Set<AnalyticEventsConsumer> {
        return when (this) {
            ALL_CHANNELS -> unmeteredConsumers + meteredConsumers
            ENGINEERING_CHANNELS -> unmeteredConsumers
            SAMPLED_COMPREHENSIVE_CHANNELS -> unmeteredConsumers + if (isSampled) meteredConsumers else emptySet()
        }
    }

    private class AnalyticsLoggingRequest(
        val eventName: String,
        val properties: Map<String, Any>?,
        val channel: EventChannel,
    )

    companion object {
        const val FEATURE_ANALYTICS_SAMPLING = "analytics_sampling"

        private const val LOG_EVENT_CHANNEL_NAME = "LogEvent channel"
        private const val IS_SAMPLED_FOR_METERED_CHANNEL = "Is in Sampled Cohort"
    }
}

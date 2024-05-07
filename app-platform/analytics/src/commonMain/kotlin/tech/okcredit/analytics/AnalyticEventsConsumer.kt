package tech.okcredit.analytics

interface AnalyticEventsConsumer {

    fun setIdentity(id: String) {}

    fun setUserProperty(properties: Map<String, Any>) {}

    fun setDeviceProperty(properties: Map<String, Any>) {}

    fun trackEvent(eventName: String, properties: Map<String, Any>?)

    val metered: Boolean
}

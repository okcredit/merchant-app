package tech.okcredit.customization.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Action(
    @SerialName(value = "event")
    val event: String? = null,
    @SerialName(value = "properties")
    val properties: Map<String, String> = mapOf(),
    @SerialName(value = "action")
    val action: String,
    @SerialName(value = "url")
    val url: String? = null,
)



fun Action.withDefaultProperties(targetName: String, component: Component): Action {
    val props = properties.toMutableMap().apply {
        this["target"] = targetName
        this["component_version"] = component.version
        this["component_kind"] = component.kind
        this["name"] = component.metadata?.name ?: ""
        this["feature"] = component.metadata?.feature ?: ""
        this["lang"] = component.metadata?.lang ?: ""
    }
    return copy(event = event, properties = props)
}

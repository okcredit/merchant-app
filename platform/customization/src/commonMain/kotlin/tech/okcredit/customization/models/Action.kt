package tech.okcredit.customization.models

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

sealed interface Action {
    val action: String
}

@Serializable
data class Track(
    @SerialName(value = "event")
    val event: String,
    @SerialName(value = "properties")
    val properties: Map<String, String> = mapOf(),
    @SerialName(value = "action")
    override val action: String
) : Action {

    companion object {
        const val NAME = "track"
    }
}

@Serializable
data class Navigate(
    @SerialName(value = "url")
    val url: String?,
    @SerialName(value = "action")
    override val action: String
) : Action {

    companion object {
        const val NAME = "navigate"
    }
}

object ActionSerializer : JsonContentPolymorphicSerializer<Action>(Action::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Action> {
        return when {
            "event" in element.jsonObject -> Track.serializer()
            else -> Navigate.serializer()
        }
    }
}


fun Track.withDefaultProperties(targetName: String, component: Component): Track {
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

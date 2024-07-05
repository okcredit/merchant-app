package tech.okcredit.customization.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TargetComponent(
    @SerialName("component")
    val component: Component,
    @SerialName("target")
    val target: String,
)

@Serializable
data class Component(
    @SerialName("kind")
    val kind: String,
    @SerialName("event_handlers")
    val eventHandlers: EventHandlers?,
    @SerialName("icon")
    val icon: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("metadata")
    val metadata: Metadata?,
    @SerialName("title")
    val title: String?,
    @SerialName("version")
    val version: String,
    @SerialName("items")
    val items: List<Component>?,
)

@Serializable
data class EventHandlers(
    @SerialName("click")
    val click: List<Action>?,
    @SerialName("view")
    val view: List<Action>?,
)

@Serializable
data class Metadata(
    @SerialName(value = "name")
    val name: String?,
    @SerialName(value = "feature")
    val feature: String? = null,
    @SerialName(value = "lang")
    val lang: String?,
    @SerialName(value = "duration")
    val duration: Int? = null,
    @SerialName(value = "item_kind")
    val itemKind: String? = null,
    @SerialName(value = "span_count")
    val spanCount: Int? = null,
    @SerialName(value = "item_count")
    val itemCount: Int? = null,
)

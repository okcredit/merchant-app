package okcredit.base.syncer

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

interface OneTimeDataSyncer {
    suspend fun execute(input: JsonObject)

    fun schedule(input: JsonObject)
}

fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        null -> JsonNull
        is JsonElement -> this
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Iterable<*> -> JsonArray(this.map { it.toJsonElement() })
        // !!! key simply converted to string
        is Map<*, *> -> JsonObject(
            this.map { it.key.toString() to it.value.toJsonElement() }
                .toMap(),
        )
        // add custom convert
        else -> throw Exception("${this::class}=$this}")
    }
}

fun JsonElement.toStringOrNull() = if (this is JsonPrimitive && isString) content else null

fun JsonElement.toIntOrNull() = if (this is JsonPrimitive) content.toIntOrNull() else null

fun JsonElement.toLongOrNull() = if (this is JsonPrimitive) content.toLongOrNull() else null

fun JsonElement.toBooleanOrNull(): Boolean? {
    return if (this is JsonPrimitive) {
        if (content.equals("true", ignoreCase = true)) {
            true
        } else if (content.equals("false", ignoreCase = true)) {
            false
        } else {
            null
        }
    } else {
        null
    }
}

fun JsonElement.toAnyOrNull(): Any? {
    return when (this) {
        is JsonNull -> null
        is JsonPrimitive -> toAnyValue()
        // !!! key convert back custom object
        is JsonObject -> this.map { it.key to it.value.toAnyOrNull() }.toMap()
        is JsonArray -> this.map { it.toAnyOrNull() }
    }
}

private fun Any?.toJsonPrimitive(): JsonPrimitive {
    return when (this) {
        null -> JsonNull
        is JsonPrimitive -> this
        is Boolean -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        // add custom convert
        else -> throw Exception("不支持类型:${this::class}")
    }
}

private fun JsonPrimitive.toAnyValue(): Any? {
    val content = this.content
    if (this.isString) {
        // add custom string convert
        return content
    }
    if (content.equals("null", ignoreCase = true)) {
        return null
    }
    if (content.equals("true", ignoreCase = true)) {
        return true
    }
    if (content.equals("false", ignoreCase = true)) {
        return false
    }
    val intValue = content.toIntOrNull()
    if (intValue != null) {
        return intValue
    }
    val longValue = content.toLongOrNull()
    if (longValue != null) {
        return longValue
    }
    val doubleValue = content.toDoubleOrNull()
    if (doubleValue != null) {
        return doubleValue
    }
    throw Exception(content)
}

fun Map<String, Any?>.toJsonObject(): JsonObject {
    return JsonObject(
        content = this.entries.associate {
            it.key to it.value.toJsonElement()
        },
    )
}

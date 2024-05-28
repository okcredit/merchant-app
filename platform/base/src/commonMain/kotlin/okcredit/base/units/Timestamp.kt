package okcredit.base.units

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Timestamp(val epochMillis: Long) {
    operator fun compareTo(other: Timestamp) = epochMillis.compareTo(other.epochMillis)
}

inline val Long.timestamp: Timestamp
    get() = Timestamp(this)

inline val Int.timestamp: Timestamp
    get() = Timestamp(this.toLong())

inline val Instant.timestamp: Timestamp
    get() = Timestamp(toEpochMilliseconds())

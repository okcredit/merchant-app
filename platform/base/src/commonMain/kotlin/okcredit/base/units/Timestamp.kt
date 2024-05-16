package okcredit.base.units

import kotlin.jvm.JvmInline

@JvmInline
value class Timestamp(val epoch: Long) {
    operator fun compareTo(other: Timestamp) = epoch.compareTo(other.epoch)
}

inline val Long.timestamp: Timestamp
    get() = Timestamp(this)
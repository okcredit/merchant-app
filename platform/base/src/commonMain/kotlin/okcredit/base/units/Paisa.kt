package okcredit.base.units

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Paisa(val value: Long) {
    operator fun plus(other: Paisa) = Paisa(value + other.value)

    operator fun minus(other: Paisa) = Paisa(value - other.value)

    operator fun compareTo(other: Paisa) = value.compareTo(other.value)

    operator fun times(other: Paisa) = Paisa(value * other.value)

    operator fun div(other: Paisa) = Paisa(value / other.value)

    operator fun unaryMinus() = Paisa(-value)

    operator fun unaryPlus() = Paisa(+value)

    operator fun inc() = Paisa(value + 1)

    operator fun dec() = Paisa(value - 1)

    operator fun rem(other: Paisa) = Paisa(value % other.value)

    operator fun rangeTo(other: Paisa) = value..other.value

    override fun toString() = formatPaisa(value, withRupeePrefix = true)

    companion object {
        val ZERO = Paisa(0)
    }
}

operator fun Paisa.compareTo(other: Long) = value.compareTo(other)

operator fun Long.compareTo(other: Paisa) = compareTo(other.value)

operator fun Paisa.plus(other: Long) = Paisa(value + other)

operator fun Long.plus(other: Paisa) = Paisa(this + other.value)

operator fun Paisa.minus(other: Long) = Paisa(value - other)

operator fun Long.minus(other: Paisa) = Paisa(this - other.value)

fun formatPaisa(amount: Long, withRupeePrefix: Boolean = false): String {
    if (amount == 0L) {
        return buildString {
            if (withRupeePrefix) {
                append("₹")
            }
            append(0)
        }
    }
    var amountTemp = amount
    // find the absolute value if amount is negative
    if (amountTemp < 0L) {
        amountTemp *= -1
    }
    // get the fraction amount and find the string to append in the end
    val fraction = amountTemp % 100
    val fractionString: String = when {
        fraction == 0L -> {
            ""
        }
        fraction < 10 -> {
            ".0$fraction"
        }
        else -> {
            ".$fraction"
        }
    }
    amountTemp /= 100
    // Example amount - 12,34,56,789
    val hundredth = amountTemp % 1000 // returns 789
    val thousandth = ((amountTemp / 1000).toInt()) % 100 // returns 56
    val lakth = ((amountTemp / 1_00_000).toInt()) % 100 // returns 34
    val croreth = ((amountTemp / 1_00_00_000).toInt()) // returns 12
    return buildString {
        if (withRupeePrefix) {
            append("₹")
        }

        if (croreth > 0) {
            append(croreth)
            append(",")
        }

        if (lakth > 0) {
            // append single 0 if lakh is a single digit but total amount is greater than crore
            if (croreth > 0 && lakth < 10) {
                append("0")
            }
            append(lakth)
            append(",")
        } else {
            // append 00 if lakh is zero but total amount is greater than crore
            if (croreth > 0) {
                append("00")
                append(",")
            }
        }

        if (thousandth > 0) {
            // append single 0 if thousandth is a single digit but total amount is greater than lakh
            if ((croreth > 0 || lakth > 0) && thousandth < 10) {
                append("0")
            }
            append(thousandth)
            append(",")
        } else {
            // append double 0 if thousandth is zero but total amount is greater than lakh
            if (croreth > 0 || lakth > 0) {
                append("00")
                append(",")
            }
        }

        if (hundredth > 0) {
            if (croreth > 0 || lakth > 0 || thousandth > 0) {
                if (hundredth < 10) { // append double 0 if hundredth is single digit but total amount is greater than thousand
                    append("00")
                } else if (hundredth < 100) { // append single 0 if hundredth is double digit but total amount is greater than thousand
                    append("0")
                }
            }
            append(hundredth)
        } else {
            // append triple 0 if hundredth is zero but total amount is greater than thousand
            if (croreth > 0 || lakth > 0 || thousandth > 0) {
                append("000")
            }
        }

        // finally append 0 if we just have amount just in fractions. For eg - 0.35
        val currentDigits = if (withRupeePrefix) length - 1 else length
        if (fraction > 0 && currentDigits == 0) {
            append("0")
        }
        append(fractionString)
    }
}

inline val Long.paisa: Paisa
    get() = Paisa(this)

inline val Int.paisa: Paisa
    get() = Paisa(this.toLong())

inline val Double.paisa: Paisa
    get() = Paisa((this * 100).toLong())

inline val Float.paisa: Paisa
    get() = Paisa((this * 100).toLong())

fun Paisa.toRupees(): Double {
    return this.value.toDouble() / 100
}

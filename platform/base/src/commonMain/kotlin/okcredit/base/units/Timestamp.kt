package okcredit.base.units

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlinx.datetime.yearsUntil
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.math.absoluteValue

@Serializable
@JvmInline
value class Timestamp(val epochMillis: Long) {
    operator fun compareTo(other: Timestamp) = epochMillis.compareTo(other.epochMillis)

    fun format(format: String = "dd MMM yyyy"): String {
        val localDate = Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        when (format) {
            "dd MMM yyyy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.abbreviation
                val year = localDate.year
                return "$date $month $year"
            }

            "dd MMM yyyy, hh:mm aa" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.abbreviation
                val year = localDate.year
                val hours = localDate.hour
                val minutes = localDate.minute
                val amPm = if (hours < 12) "AM" else "PM"
                val adjustedHour = if (hours == 0 || hours == 12) 12 else hours % 12
                return "$date $month $year, $adjustedHour:$minutes $amPm"
            }

            "dd MMM" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.abbreviation
                return "$date $month"
            }

            "dd/MM/yyyy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                val year = localDate.year
                return "$date/$month/$year"
            }

            "dd/MM" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                return "$date/$month"
            }

            "dd-MM-yyyy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                val year = localDate.year
                return "$date-$month-$year"
            }

            "dd-MM" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                return "$date-$month"
            }

            "dd-MM-yy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                val year = localDate.year.toString().takeLast(2)
                return "$date-$month-$year"
            }

            "mmm dd, yyyy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.abbreviation
                val year = localDate.year
                return "$month $date, $year"
            }

            "MMM dd, yyyy" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.abbreviation
                val year = localDate.year
                return "$month $date, $year"
            }

            "yyyy-mm-dd" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                val year = localDate.year
                return "$year-$month-$date"
            }

            "yyyy-MM-dd" -> {
                val date = localDate.dayOfMonth
                val month = localDate.month.number
                val year = localDate.year
                return "$year-$month-$date"
            }

            else -> return localDate.toString()
        }
    }

    fun relativeDate(): String {
        val now = Clock.System.now()
        val localDate = Instant.fromEpochMilliseconds(epochMillis)
        val days = localDate.daysUntil(now, TimeZone.currentSystemDefault()).absoluteValue
        return when (days) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> format("dd MMM yyyy")
        }
    }

    fun relativeTime(): String {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val hours = localDateTime.hour
        val minutes = localDateTime.minute
        val amPm = if (hours < 12) "AM" else "PM"

        val adjustedHour = if (hours == 0 || hours == 12) 12 else hours % 12
        return "$adjustedHour:$minutes $amPm"
    }

    fun relativeDateAndTime(): String {
        val now = Clock.System.now()
        val localDate = Instant.fromEpochMilliseconds(epochMillis)
        if (localDate.daysUntil(now, TimeZone.currentSystemDefault()).absoluteValue == 0) {
            return "Today ${relativeTime()}"
        }
        return format("dd MMM yyyy, hh:mm aa")
    }

    fun relativeDateIncludingTime(): String {
        val now = Clock.System.now()
        val localDate = Instant.fromEpochMilliseconds(epochMillis)
        return when (val minuteDifference =
            localDate.until(now, DateTimeUnit.MINUTE).absoluteValue) {
            0L -> "Just Now"
            in 1..59 -> "$minuteDifference min ago"
            in 60..(60 * 24) -> {
                val hours = minuteDifference / 60
                "$hours hrs ago"
            }

            else -> relativeDate()
        }
    }

    companion object {

        fun parse(date: String): Timestamp {
            val dateParts = date.split("-")
            val localDate = LocalDate(
                year = dateParts[0].toInt(),
                monthNumber = dateParts[1].toInt(),
                dayOfMonth = dateParts[2].toInt(),
            )
            return localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).timestamp
        }
    }
}

private val Month.abbreviation: String
    get() {
        return when (this) {
            Month.JANUARY -> "Jan"
            Month.FEBRUARY -> "Feb"
            Month.MARCH -> "Mar"
            Month.APRIL -> "Apr"
            Month.MAY -> "May"
            Month.JUNE -> "Jun"
            Month.JULY -> "Jul"
            Month.AUGUST -> "Aug"
            Month.SEPTEMBER -> "Sep"
            Month.OCTOBER -> "Oct"
            Month.NOVEMBER -> "Nov"
            Month.DECEMBER -> "Dec"
            else -> "Unknown"
        }
    }
inline val Long.timestamp: Timestamp
    get() = Timestamp(this)

inline val Int.timestamp: Timestamp
    get() = Timestamp(this.toLong())

inline val Instant.timestamp: Timestamp
    get() = Timestamp(toEpochMilliseconds())

val ZERO_TIMESTAMP = Timestamp(0)

inline val Timestamp.isZero: Boolean
    get() = epochMillis == 0L

inline val Timestamp.instant: Instant
    get() = Instant.fromEpochMilliseconds(epochMillis)

fun Timestamp.differenceInDays(): Int {
    val now = Clock.System.now()
    return this.instant.daysUntil(now, TimeZone.currentSystemDefault())
}

fun Int.formattedDaysDifference(): String {
    val days = this.absoluteValue
    return when {
        days == 0 -> ""
        days == 1 -> "1 day"
        days < 30 -> "$days days"
        days < 60 -> "1 month"
        days < 365 -> "${days / 30} months"
        else -> "${days / 365} years"
    }
}

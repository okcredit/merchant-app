package app.okcredit.ledger.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {
    // todo check with @mohitesh, this might not work in iOS
    fun getCurrentTime(): Instant {
        return Clock.System.now()
    }

    fun getTimeOnly(time: Instant): String {
        val localDateTime = time.toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = localDateTime.hour % 12
        val minute = localDateTime.minute
        val amPm = if (localDateTime.hour >= 12) "PM" else "AM"
        val formattedHour = if (hour == 0) 12 else hour
        val formattedMinute = minute.toString().padStart(2, '0')
        return "$formattedHour:$formattedMinute $amPm"
    }

    fun isSameDay(lastDate: Long, currentDate: Long): Boolean {
        return Instant.fromEpochMilliseconds(lastDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds() == Instant.fromEpochMilliseconds(currentDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    fun isSevenDaysPassed(billDate: Instant): Boolean {
        return billDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.daysUntil(
            getCurrentTime().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        ) > 7
    }

    fun formatDateOnly(dateTime: Instant): String {
        val localDateTime = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val year = localDateTime.year
        return "$day $month $year"
    }
}

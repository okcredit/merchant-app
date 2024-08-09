package app.okcredit.ledger.ui.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateTimeUtils {

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
        return Instant.fromEpochMilliseconds(epochMilliseconds = lastDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date == Instant.fromEpochMilliseconds(
            epochMilliseconds = currentDate
        ).toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    fun formatDateOnly(dateTime: Instant): String {
        val localDateTime = dateTime.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }
        val year = localDateTime.year
        return "$day $month $year"
    }
}
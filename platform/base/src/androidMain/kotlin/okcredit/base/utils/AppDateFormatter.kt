package okcredit.base.utils

import android.text.format.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

@Inject
actual class AppDateFormatter(
    private val locale: Locale = Locale.ENGLISH,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
) {

    private val shortDateFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale)
    }

    private val shortTimeFormatter: DateFormat by lazy {
        SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, locale)
    }
    private val mediumDateFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, locale)
    }
    private val mediumDateTimeFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, locale)
    }

    private val sDateTimeFormatter: DateFormat by lazy {
        SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, locale)
    }

    actual fun formatShortDate(instant: Instant): String {
        return shortDateFormatter.format(instant.toJavaDate())
    }

    actual fun formatShortDate(date: LocalDate): String {
        return shortDateFormatter.format(date.toJavaDate())
    }

    fun LocalDate.toJavaDate(): Date {
        return Date(atStartOfDayIn(timeZone).toEpochMilliseconds())
    }

    fun Instant.toJavaDate(): Date {
        return Date(toEpochMilliseconds())
    }

    actual fun formatMediumDate(instant: Instant): String {
        return mediumDateFormatter.format(instant.toJavaDate())
    }

    actual fun formatMediumDateTime(instant: Instant): String {
        return mediumDateTimeFormatter.format(instant.toJavaDate())
    }

    actual fun formatShortTime(localTime: LocalTime): String {
        val current = Clock.System.now().toLocalDateTime(timeZone)
        val date = localTime.atDate(current.year, current.monthNumber, current.dayOfMonth)
        return shortTimeFormatter.format(date.toInstant(timeZone).toJavaDate())
    }

    actual fun formatShortRelativeTime(date: Instant, reference: Instant): String = when {
        // Within the past week
        date < reference && (reference - date) < 7.days -> {
            DateUtils.getRelativeTimeSpanString(
                date.toEpochMilliseconds(),
                reference.toEpochMilliseconds(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_SHOW_DATE,
            ).toString()
        }
        // In the near future (next 2 weeks)
        date > reference && (date - reference) < 14.days -> {
            DateUtils.getRelativeTimeSpanString(
                date.toEpochMilliseconds(),
                reference.toEpochMilliseconds(),
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_SHOW_DATE,
            ).toString()
        }
        // In the far past/future
        else -> formatMediumDate(date)
    }
}

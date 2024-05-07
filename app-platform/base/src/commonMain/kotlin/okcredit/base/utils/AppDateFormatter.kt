package okcredit.base.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

expect class AppDateFormatter {
    fun formatShortDate(instant: Instant): String
    fun formatShortDate(date: LocalDate): String
    fun formatMediumDate(instant: Instant): String
    fun formatMediumDateTime(instant: Instant): String
    fun formatShortTime(localTime: LocalTime): String
    fun formatShortRelativeTime(date: Instant, reference: Instant = Clock.System.now()): String
}

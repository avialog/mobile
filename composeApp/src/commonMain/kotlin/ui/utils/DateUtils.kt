package ui.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

fun LocalDate.formatDayMonthYear(): String {
    return LocalDate.Format {
        dayOfMonth()
        char('.')
        monthNumber()
        char('.')
        year()
    }.format(this)
}

fun LocalTime.formatHourMinute(): String {
    return LocalTime.Format {
        hour()
        char(':')
        minute()
    }.format(this)
}

fun LocalDate.toEpochMillisecondsAsUtc() =
    atTime(
        time = LocalTime.fromMillisecondOfDay(0),
    ).toInstant(timeZone = TimeZone.UTC).toEpochMilliseconds()

package ui.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

fun LocalDate.formatDayMonthYear(): String {
    return LocalDate.Format {
        dayOfMonth()
        char(',')
        monthNumber()
        char(',')
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

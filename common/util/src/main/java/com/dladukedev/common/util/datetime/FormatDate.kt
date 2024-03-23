package com.dladukedev.common.util.datetime

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface FormatDate {
    operator fun invoke(date: LocalDate, format: DateFormat): String

}

enum class DateFormat(val pattern: String) {
    MonthStringShortAndDateLongInt("MMM dd"),
    MonthLongStringDateShortIntAndYearLongInt("MMMM d, YYYY")
}

class FormatDateUseCase @Inject constructor(): FormatDate {
    override fun invoke(date: LocalDate, format: DateFormat): String {
        val formatter = DateTimeFormatter.ofPattern(format.pattern)
        return date.format(formatter)
    }
}
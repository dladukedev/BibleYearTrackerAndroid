package com.dladukedev.bibleyeartracker.common

import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class FormatDateTest {
    @Test
    fun `formats MonthStringShortAndDateLongInt correctly`() {
        // Given
        val date = LocalDate.of(2000, Month.JANUARY, 28)
        val format = DateFormat.MonthStringShortAndDateLongInt
        val expected = "Jan 28"
        val formatDate = FormatDateUseCase()

        // When
        val actual = formatDate(date, format)

        // Then
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `formats MonthLongStringDateShortIntAndYearLongInt correctly`() {
        // Given
        val date = LocalDate.of(2000, Month.JANUARY, 28)
        val format = DateFormat.MonthLongStringDateShortIntAndYearLongInt
        val expected = "January 28, 2000"
        val formatDate = FormatDateUseCase()

        // When
        val actual = formatDate(date, format)

        // Then
        Assert.assertEquals(expected, actual)
    }

}
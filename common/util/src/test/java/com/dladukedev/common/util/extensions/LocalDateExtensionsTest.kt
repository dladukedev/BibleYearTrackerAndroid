package com.dladukedev.common.util.extensions

import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class LocalDateExtensionsTest {
    @Test
    fun `toMilliseconds returns the LocalDate in milliseconds since 1-1-1970 at noon`() {
        // Given
        val date = LocalDate.of(1970, Month.JANUARY, 1)
        val expected = 43200000L

        // When
        val actual = date.toMilliseconds()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `asLocalDate returns the Long as the LocalDate in UTC if no zone provided`() {
        // Given
        val milliseconds = 43200000L
        val expected = LocalDate.of(1970, Month.JANUARY, 1)

        // When
        val actual = milliseconds.asLocalDate()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `asLocalDate returns the Long as the LocalDate adjusted for the provided zone`() {
        // Given
        val milliseconds = 43200000L
        val expected = LocalDate.of(1970, Month.JANUARY, 2)

        // When
        val actual = milliseconds.asLocalDate("Etc/GMT-14")

        // Then
        Assert.assertEquals(expected, actual)
    }
}
package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingDay
import org.junit.Assert
import org.junit.Test

class BibleDataCacheDataSourceTest {
    @Test
    fun `setBibleData calls successfully`() {
        // Given
        val cache = BibleDataInMemoryCacheDataSource()
        val bibleReadings = listOf(
            BibleReadingDay(1, 0, emptyList()),
            BibleReadingDay(2, 1, emptyList()),
            BibleReadingDay(3, 2, emptyList()),
        )

        // When
        cache.setBibleData(bibleReadings)

        // Then
    }

    @Test
    fun `getBibleData returns null if cache is uninitialized`() {
        // Given
        val cache = BibleDataInMemoryCacheDataSource()
        val expected = null

        // When
        val actual = cache.getBibleData()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getBibleData returns values from cache set by setBibleData`() {
        // Given
        val cache = BibleDataInMemoryCacheDataSource()
        val bibleReadings = listOf(
            BibleReadingDay(1, 0, emptyList()),
            BibleReadingDay(2, 1, emptyList()),
            BibleReadingDay(3, 2, emptyList()),
        )
        val expected = bibleReadings

        // When
        cache.setBibleData(bibleReadings)
        val actual = cache.getBibleData()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `setBibleData resets the current cache value`() {
        // Given
        val cache = BibleDataInMemoryCacheDataSource()
        val bibleReadingsBefore = listOf(
            BibleReadingDay(1, 0, emptyList()),
            BibleReadingDay(2, 1, emptyList()),
            BibleReadingDay(3, 2, emptyList()),
        )
        val bibleReadingsAfter = listOf(
            BibleReadingDay(4, 3, emptyList()),
            BibleReadingDay(5, 4, emptyList()),
            BibleReadingDay(6, 5, emptyList()),
        )
        val expected = bibleReadingsAfter

        // When
        cache.setBibleData(bibleReadingsBefore)
        cache.setBibleData(bibleReadingsAfter)
        val actual = cache.getBibleData()

        // Then
        Assert.assertEquals(expected, actual)
    }
}
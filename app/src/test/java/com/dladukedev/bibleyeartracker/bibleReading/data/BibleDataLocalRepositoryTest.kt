package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleBook
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingDay
import com.dladukedev.bibleyeartracker.bibleReading.domain.Reading
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class BibleDataLocalRepositoryTest {
    @Test
    fun `getBibleReadings returns cached values if present`() = runTest {
        // Given
        val cachedData = listOf(
            BibleReadingDay(1, 0, emptyList()),
            BibleReadingDay(2, 1, emptyList()),
            BibleReadingDay(3, 2, emptyList()),
        )
        val cacheDataSource = object : BibleDataCacheDataSource {
            override fun getBibleData(): List<BibleReadingDay>? = cachedData
            override fun setBibleData(bibleData: List<BibleReadingDay>) =
                TODO("Not yet implemented")
        }
        val localDataSource = object : BibleDataLocalDataSource {
            override suspend fun getBibleData(): List<BibleReadingDataModel> =
                TODO("Not yet implemented")
        }
        val bibleReadingParser = object : BibleReadingParser {
            override fun parse(rawReading: String): List<Reading> = TODO("Not yet implemented")
        }
        val bibleDataLocalRepository = BibleDataLocalRepository(
            cacheDataSource = cacheDataSource,
            localDataSource = localDataSource,
            bibleReadingParser = bibleReadingParser,
        )
        val expected = cachedData


        // When
        val actual = bibleDataLocalRepository.getBibleReadings()

        // Then
        Assert.assertEquals(expected, actual)
    }
    // getBibleReadings

    // gets values from local source if cache empty
    @Test
    fun `getBibleReadings gets values from local source if cache is empty`() = runTest {
        // Given
        val cacheDataSource = object : BibleDataCacheDataSource {
            override fun getBibleData(): List<BibleReadingDay>? = null
            override fun setBibleData(bibleData: List<BibleReadingDay>) {}
        }
        val localDataSource = object : BibleDataLocalDataSource {
            override suspend fun getBibleData(): List<BibleReadingDataModel> = listOf(
                BibleReadingDataModel(1, 0, "READING_1", "READING_2", "READING_3")
            )
        }
        val bibleReadingParser = object : BibleReadingParser {
            override fun parse(rawReading: String): List<Reading> = when (rawReading) {
                "READING_1" -> listOf(Reading.WholeBook(BibleBook.GENESIS))
                "READING_2" -> listOf(Reading.WholeBook(BibleBook.EXODUS))
                "READING_3" -> listOf(Reading.WholeBook(BibleBook.LEVITICUS))
                else -> TODO("Not yet implemented")
            }
        }
        val bibleDataLocalRepository = BibleDataLocalRepository(
            cacheDataSource = cacheDataSource,
            localDataSource = localDataSource,
            bibleReadingParser = bibleReadingParser,
        )
        val expected = listOf(
            BibleReadingDay(
                1, 0, listOf(
                    Reading.WholeBook(BibleBook.GENESIS),
                    Reading.WholeBook(BibleBook.EXODUS),
                    Reading.WholeBook(BibleBook.LEVITICUS),
                )
            )
        )

        // When
        val actual = bibleDataLocalRepository.getBibleReadings()

        // Then
        Assert.assertEquals(expected, actual)
    }

    // updates cache after loading data
    @Test
    fun `getBibleReadings updates cache after loading data`() = runTest {
        // Given
        val cacheDataSource = object : BibleDataCacheDataSource {
            private var cached: List<BibleReadingDay>? = null
            override fun getBibleData(): List<BibleReadingDay>? = cached
            override fun setBibleData(bibleData: List<BibleReadingDay>) {
                cached = bibleData
            }
        }
        val localDataSource = object : BibleDataLocalDataSource {
            override suspend fun getBibleData(): List<BibleReadingDataModel> = listOf(
                BibleReadingDataModel(1, 0, "READING_1", "READING_2", "READING_3")
            )
        }
        val bibleReadingParser = object : BibleReadingParser {
            override fun parse(rawReading: String): List<Reading> = when (rawReading) {
                "READING_1" -> listOf(Reading.WholeBook(BibleBook.GENESIS))
                "READING_2" -> listOf(Reading.WholeBook(BibleBook.EXODUS))
                "READING_3" -> listOf(Reading.WholeBook(BibleBook.LEVITICUS))
                else -> TODO("Not yet implemented")
            }
        }
        val bibleDataLocalRepository = BibleDataLocalRepository(
            cacheDataSource = cacheDataSource,
            localDataSource = localDataSource,
            bibleReadingParser = bibleReadingParser,
        )
        val expected = listOf(
            BibleReadingDay(
                1, 0, listOf(
                    Reading.WholeBook(BibleBook.GENESIS),
                    Reading.WholeBook(BibleBook.EXODUS),
                    Reading.WholeBook(BibleBook.LEVITICUS),
                )
            )
        )

        // When
        bibleDataLocalRepository.getBibleReadings()
        val actual = cacheDataSource.getBibleData()

        // Then
        Assert.assertEquals(expected, actual)
    }


    // getBibleReading
    @Test
    fun `getReading returns value if present`() = runTest {
        // Given
        val cacheDataSource = object : BibleDataCacheDataSource {
            override fun getBibleData(): List<BibleReadingDay>? = listOf(
                BibleReadingDay(1, 0, emptyList()),
                BibleReadingDay(2, 1, emptyList()),
                BibleReadingDay(3, 2, emptyList()),
            )

            override fun setBibleData(bibleData: List<BibleReadingDay>) =
                TODO("Not yet implemented")
        }
        val localDataSource = object : BibleDataLocalDataSource {
            override suspend fun getBibleData(): List<BibleReadingDataModel> =
                TODO("Not yet implemented")
        }
        val bibleReadingParser = object : BibleReadingParser {
            override fun parse(rawReading: String): List<Reading> = TODO("Not yet implemented")
        }
        val bibleDataLocalRepository = BibleDataLocalRepository(
            cacheDataSource = cacheDataSource,
            localDataSource = localDataSource,
            bibleReadingParser = bibleReadingParser,
        )
        val expected = BibleReadingDay(1, 0, emptyList())

        // When
        val actual = bibleDataLocalRepository.getReading(1)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `getReading returns null if value is not present`() = runTest {
        // Given
        val cacheDataSource = object : BibleDataCacheDataSource {
            override fun getBibleData(): List<BibleReadingDay>? = listOf(
                BibleReadingDay(1, 0, emptyList()),
                BibleReadingDay(2, 1, emptyList()),
                BibleReadingDay(3, 2, emptyList()),
            )

            override fun setBibleData(bibleData: List<BibleReadingDay>) =
                TODO("Not yet implemented")
        }
        val localDataSource = object : BibleDataLocalDataSource {
            override suspend fun getBibleData(): List<BibleReadingDataModel> =
                TODO("Not yet implemented")
        }
        val bibleReadingParser = object : BibleReadingParser {
            override fun parse(rawReading: String): List<Reading> = TODO("Not yet implemented")
        }
        val bibleDataLocalRepository = BibleDataLocalRepository(
            cacheDataSource = cacheDataSource,
            localDataSource = localDataSource,
            bibleReadingParser = bibleReadingParser,
        )
        val expected = null

        // When
        val actual = bibleDataLocalRepository.getReading(10)

        // Then
        Assert.assertEquals(expected, actual)
    }
}
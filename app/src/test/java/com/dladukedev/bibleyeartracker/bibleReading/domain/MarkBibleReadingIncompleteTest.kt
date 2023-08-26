package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class MarkBibleReadingIncompleteTest {
    @Test
    fun `returns true when the bible reading is marked incomplete`() = runTest {
        // Given
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> = emptyList()
            override suspend fun getReading(readingId: Int): BibleReadingDay? {
                return BibleReadingDay(id = 1, offset = 2, readings = emptyList())
            }
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {}
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
        }
        val canMarkBibleReadingIncomplete = object : CanMarkBibleReadingIncomplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = true
        }
        val markBiblReadingIncomplete = MarkBibleReadingIncompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingIncomplete = canMarkBibleReadingIncomplete,
        )
        val expected = true

        // When
        val actual = markBiblReadingIncomplete(1)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `updates the lastReadOffset when the bible reading is marked incomplete`() = runTest {
        // Given
        val bibleReading = BibleReadingDay(id = 1, offset = 2, readings = emptyList())
        val previousOffset = bibleReading.offset - 1
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> = emptyList()
            override suspend fun getReading(readingId: Int): BibleReadingDay? = bibleReading
        }
        val readInformationRepository = object : ReadInformationRepository {
            var setLastReadOffsetCalled = false
            var setLastReadOffsetCalledWith: Int? = -1
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {}
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {
                setLastReadOffsetCalled = true
                setLastReadOffsetCalledWith = lastReadOffset
            }
        }
        val canMarkBibleReadingIncomplete = object : CanMarkBibleReadingIncomplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = true
        }
        val markBiblReadingIncomplete = MarkBibleReadingIncompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingIncomplete = canMarkBibleReadingIncomplete,
        )

        // When
        markBiblReadingIncomplete(1)

        // Then
        Assert.assertTrue(readInformationRepository.setLastReadOffsetCalled)
        Assert.assertEquals(
            previousOffset,
            readInformationRepository.setLastReadOffsetCalledWith
        )
    }

    @Test
    fun `returns false when the bible reading can't be found`() = runTest {
        // Given
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> = emptyList()
            override suspend fun getReading(readingId: Int): BibleReadingDay? = null
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {}
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
        }
        val canMarkBibleReadingIncomplete = object : CanMarkBibleReadingIncomplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = true
        }
        val markBiblReadingIncomplete = MarkBibleReadingIncompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingIncomplete = canMarkBibleReadingIncomplete,
        )
        val expected = false

        // When
        val actual = markBiblReadingIncomplete(1)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `returns false when the bible reading can't be marked incomplete`() = runTest {
        // Given
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> = emptyList()
            override suspend fun getReading(readingId: Int): BibleReadingDay? {
                return BibleReadingDay(id = 1, offset = 2, readings = emptyList())
            }
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {}
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
        }
        val canMarkBibleReadingIncomplete = object : CanMarkBibleReadingIncomplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = false
        }
        val markBiblReadingIncomplete = MarkBibleReadingIncompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingIncomplete = canMarkBibleReadingIncomplete,
        )
        val expected = false

        // When
        val actual = markBiblReadingIncomplete(1)

        // Then
        Assert.assertEquals(expected, actual)
    }
}
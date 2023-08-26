package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class MarkBibleReadingCompleteTest {
    @Test
    fun `returns true when the bible reading is marked complete`() = runTest {
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
        val canMarkBibleReadingComplete = object : CanMarkBibleReadingComplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = true
        }
        val markBiblReadingComplete = MarkBibleReadingCompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingComplete = canMarkBibleReadingComplete,
        )
        val expected = true

        // When
        val actual = markBiblReadingComplete(0)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `updates the lastReadOffset when the bible reading is marked complete`() = runTest {
        // Given
        val bibleReading = BibleReadingDay(id = 1, offset = 2, readings = emptyList())
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
        val canMarkBibleReadingComplete = object : CanMarkBibleReadingComplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = true
        }
        val markBiblReadingComplete = MarkBibleReadingCompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingComplete = canMarkBibleReadingComplete,
        )
        val expected = true

        // When
        markBiblReadingComplete(0)

        // Then
        Assert.assertTrue(readInformationRepository.setLastReadOffsetCalled)
        Assert.assertEquals(
            bibleReading.offset,
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
        val canMarkBibleReadingComplete = object : CanMarkBibleReadingComplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = false
        }
        val markBiblReadingComplete = MarkBibleReadingCompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingComplete = canMarkBibleReadingComplete,
        )
        val expected = false

        // When
        val actual = markBiblReadingComplete(0)

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `returns false when the bible reading can't be marked complete`() = runTest {
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
        val canMarkBibleReadingComplete = object : CanMarkBibleReadingComplete {
            override suspend fun invoke(reading: BibleReadingDay): Boolean = false
        }
        val markBiblReadingComplete = MarkBibleReadingCompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
            canMarkBibleReadingComplete = canMarkBibleReadingComplete,
        )
        val expected = false

        // When
        val actual = markBiblReadingComplete(0)

        // Then
        Assert.assertEquals(expected, actual)
    }
}
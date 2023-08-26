package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CanMarkBibleReadingIncompleteTest {
    @Test
    fun `returns true when the previous offset equals the reading to be marked incomplete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 11
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingIncomplete = CanMarkBibleReadingIncompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = true


            // When
            val actual = canMarkBibleReadingIncomplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `returns false when the previous offset is null`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = null
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingIncomplete = CanMarkBibleReadingIncompleteUseCase(repository)
            val reading = BibleReadingDay(1, 0, emptyList())
            val expected = false

            // When
            val actual = canMarkBibleReadingIncomplete(reading)

            // Then
            Assert.assertEquals(expected, actual)

        }

    @Test
    fun `returns false when the previous offset is before the reading to be marked complete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 10
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingIncomplete = CanMarkBibleReadingIncompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = false


            // When
            val actual = canMarkBibleReadingIncomplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `returns false when the previous offset is after the reading to be marked complete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 12
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingIncomplete = CanMarkBibleReadingIncompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = false


            // When
            val actual = canMarkBibleReadingIncomplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }
}
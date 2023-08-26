package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CanMarkBibleReadingCompleteTest {
    @Test
    fun `returns true when the previous offset is 1 day before the reading to be marked complete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 10
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingComplete = CanMarkBibleReadingCompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = true


            // When
            val actual = canMarkBibleReadingComplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `returns true when the previous offset is null and the reading to be marked is the first reading`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = null
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingComplete = CanMarkBibleReadingCompleteUseCase(repository)
            val reading = BibleReadingDay(1, 0, emptyList())
            val expected = true


            // When
            val actual = canMarkBibleReadingComplete(reading)

            // Then
            Assert.assertEquals(expected, actual)

        }

    @Test
    fun `returns false when the previous offset is more than 1 day before the reading to be marked complete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 2
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingComplete = CanMarkBibleReadingCompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = false


            // When
            val actual = canMarkBibleReadingComplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `returns false when the previous offset is less than 1 day before the reading to be marked complete`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = 12
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingComplete = CanMarkBibleReadingCompleteUseCase(repository)
            val reading = BibleReadingDay(12, 11, emptyList())
            val expected = false


            // When
            val actual = canMarkBibleReadingComplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

    @Test
    fun `returns false when the previous offset is null and the reading to be marked is not the first reading`() =
        runTest {
            // Given
            val repository = object : ReadInformationRepository {
                override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { }
                override suspend fun getLastReadOffset(): Int? = null
                override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
            }
            val canMarkBibleReadingComplete = CanMarkBibleReadingCompleteUseCase(repository)
            val reading = BibleReadingDay(11, 10, emptyList())
            val expected = false


            // When
            val actual = canMarkBibleReadingComplete(reading)

            // Then
            Assert.assertEquals(expected, actual)
        }

}

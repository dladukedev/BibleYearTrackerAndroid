package com.dladukedev.bibleyeartracker.bibleReading.domain

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class SubscribeToPercentCompleteTest {
    @Test
    fun `emits the percentage of readings complete`() = runBlocking {
        // Given
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> = listOf(
                BibleReadingDay(0, 0, emptyList()),
                BibleReadingDay(0, 0, emptyList()),
                BibleReadingDay(0, 0, emptyList()),
                BibleReadingDay(0, 0, emptyList()),
            )
            override suspend fun getReading(readingId: Int): BibleReadingDay? {
                return BibleReadingDay(id = 1, offset = 2, readings = emptyList())
            }
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {
                delay(1)
                emit(0)
                delay(1)
                emit(1)
                delay(1)
                emit(2)
                delay(1)
                emit(3)
            }
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {}
        }
        val subscribeToPercentComplete = SubscribeToPercentCompleteUseCase(
            bibleDataRepository = bibleDataRepository,
            readInformationRepository = readInformationRepository,
        )

        // When
        subscribeToPercentComplete().test {
            // Then
            val expected1 = 25.0
            val actual1 = awaitItem()
            Assert.assertEquals(expected1, actual1, 0.0)


            // Then
            val expected2 = 50.0
            val actual2 = awaitItem()
            Assert.assertEquals(expected2, actual2, 0.0)


            // Then
            val expected3 = 75.0
            val actual3 = awaitItem()
            Assert.assertEquals(expected3, actual3, 0.0)

            // Then
            val expected4 = 100.0
            val actual4 = awaitItem()
            Assert.assertEquals(expected4, actual4, 0.0)

            awaitComplete()
        }
    }
}
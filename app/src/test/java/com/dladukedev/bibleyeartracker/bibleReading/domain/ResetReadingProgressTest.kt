package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ResetReadingProgressTest {
    @Test
    fun `updates the lastReadOffset`() = runTest {
        // Given
        val readInformationRepository = object : ReadInformationRepository {
            var setLastReadOffsetCalled = false
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {}
            override suspend fun getLastReadOffset(): Int? = null
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {
                setLastReadOffsetCalled = true
            }
        }
        val resetReadingProgress = ResetReadingProgressUseCase(readInformationRepository)
        val expected = true

        // When
        resetReadingProgress()
        val actual = readInformationRepository.setLastReadOffsetCalled

        // Then
        Assert.assertEquals(expected, actual)
    }
}
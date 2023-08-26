package com.dladukedev.bibleyeartracker.bibleReading.data

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ReadInformationDataStoreRepositoryTest {
    @Test
    fun `subscribeGetLastReadOffset emits values from data source`() = runTest {
        // Given
        val readInformationLocalDataSource = object : ReadInformationLocalDataSource {
            override fun subscribeLastReadOffset(): Flow<Int?> = flowOf(null, 0, 1)
            override suspend fun getLastReadOffset(): Int? = TODO("Not yet implemented")
            override suspend fun setLastReadOffset(lastReadOffset: Int?) =
                TODO("Not yet implemented")
        }
        val readInformationDataStoreRepository =
            ReadInformationDataStoreRepository(readInformationLocalDataSource)

        // When
        readInformationDataStoreRepository.subscribeGetLastReadOffset().test {
            // Then
            val expectedFirst = null
            val actualFirst = awaitItem()
            Assert.assertEquals(expectedFirst, actualFirst)

            // Then
            val expectedSecond = 0
            val actualSecond = awaitItem()
            Assert.assertEquals(expectedSecond, actualSecond)

            // Then
            val expectedThird = 1
            val actualThird = awaitItem()
            Assert.assertEquals(expectedThird, actualThird)

            awaitComplete()
        }
    }

    @Test
    fun `getLastReadOffset returns the value from the data source`() = runTest {
        // Given
        val readInformationLocalDataSource = object : ReadInformationLocalDataSource {
            override fun subscribeLastReadOffset(): Flow<Int?> = TODO("Not yet implemented")
            override suspend fun getLastReadOffset(): Int? = 1
            override suspend fun setLastReadOffset(lastReadOffset: Int?) =
                TODO("Not yet implemented")
        }
        val readInformationDataStoreRepository =
            ReadInformationDataStoreRepository(readInformationLocalDataSource)
        val expected = 1

        // When
        val actual = readInformationDataStoreRepository.getLastReadOffset()

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `setLastReadOffset calls the data source with the expected value`() = runTest {
        // Given
        val readInformationLocalDataSource = object : ReadInformationLocalDataSource {
            var currentLastReadOffset: Int? = null
            override fun subscribeLastReadOffset(): Flow<Int?> = TODO("Not yet implemented")
            override suspend fun getLastReadOffset(): Int? = TODO("Not yet implemented")
            override suspend fun setLastReadOffset(lastReadOffset: Int?) {
                currentLastReadOffset = lastReadOffset
            }
        }
        val readInformationDataStoreRepository =
            ReadInformationDataStoreRepository(readInformationLocalDataSource)
        val expected = 10

        // When
        readInformationDataStoreRepository.setLastReadOffset(10)
        val actual = readInformationLocalDataSource.currentLastReadOffset

        // Then
        Assert.assertEquals(expected, actual)
    }
}
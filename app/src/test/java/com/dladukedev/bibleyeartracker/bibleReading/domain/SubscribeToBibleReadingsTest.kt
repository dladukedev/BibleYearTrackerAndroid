package com.dladukedev.bibleyeartracker.bibleReading.domain

import app.cash.turbine.test
import com.dladukedev.bibleyeartracker.common.GetCurrentDate
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.SettingsRepository
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class SubscribeToBibleReadingsTest {
    @Test
    fun `inputs map to the expected outputs`() = runTest {
        // Given
        val getCurrentDate = object : GetCurrentDate {
            override fun invoke(): LocalDate = LocalDate.of(2000, Month.JANUARY, 4)
        }
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> {
                return listOf(
                    BibleReadingDay(
                        id = 1, offset = 0, readings = listOf(
                            Reading.WholeBook(BibleBook.GENESIS),
                            Reading.WholeBook(BibleBook.EXODUS),
                            Reading.WholeBook(BibleBook.LEVITICUS),
                        )
                    ),

                    BibleReadingDay(
                        id = 2, offset = 1, readings = listOf(
                            Reading.WholeBook(BibleBook.NUMBERS),
                            Reading.WholeBook(BibleBook.DEUTERONOMY),
                            Reading.WholeBook(BibleBook.JOSHUA),
                        )
                    ),
                    BibleReadingDay(
                        id = 3, offset = 2, readings = listOf(
                            Reading.WholeBook(BibleBook.JUDGES),
                            Reading.WholeBook(BibleBook.RUTH),
                            Reading.WholeBook(BibleBook.PSALMS),
                        )
                    ),
                    BibleReadingDay(
                        id = 4, offset = 3, readings = listOf(
                            Reading.WholeBook(BibleBook.MATTHEW),
                            Reading.WholeBook(BibleBook.MARK),
                            Reading.WholeBook(BibleBook.LUKE),
                        )
                    ),
                    BibleReadingDay(
                        id = 5, offset = 4, readings = listOf(
                            Reading.WholeBook(BibleBook.JOHN),
                            Reading.WholeBook(BibleBook.ACTS),
                            Reading.WholeBook(BibleBook.ROMANS),
                        )
                    ),
                )
            }

            override suspend fun getReading(readingId: Int): BibleReadingDay? =
                TODO("Not Implemented")
        }
        val settingsRepository = object : SettingsRepository {
            override fun observeStartDate(): Flow<LocalDate> = flow {
                emit(LocalDate.of(2000, Month.JANUARY, 1))
            }

            override suspend fun setStartDate(date: LocalDate) = TODO("Not Implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not Implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not Implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not Implemented")
            override suspend fun getTheme(): Theme = TODO("Not Implemented")
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { emit(2) }
            override suspend fun getLastReadOffset(): Int? = TODO("Not Implemented")
            override suspend fun setLastReadOffset(lastReadOffset: Int?) = TODO("Not Implemented")
        }
        val subscribeToBibleReadings = SubscribeToBibleReadingsUseCase(
            getCurrentDate = getCurrentDate,
            bibleDataRepository = bibleDataRepository,
            settingsRepository = settingsRepository,
            readInformationRepository = readInformationRepository,
        )

        val expected = listOf(
            BibleReadingItem(
                id = 1,
                date = LocalDate.of(2000, Month.JANUARY, 1),
                isMarkedComplete = true,
                isToday = false,
                readings = listOf(
                    Reading.WholeBook(BibleBook.GENESIS),
                    Reading.WholeBook(BibleBook.EXODUS),
                    Reading.WholeBook(BibleBook.LEVITICUS),
                )
            ),
            BibleReadingItem(
                id = 2,
                date = LocalDate.of(2000, Month.JANUARY, 2),
                isMarkedComplete = true,
                isToday = false,
                readings = listOf(
                    Reading.WholeBook(BibleBook.NUMBERS),
                    Reading.WholeBook(BibleBook.DEUTERONOMY),
                    Reading.WholeBook(BibleBook.JOSHUA),
                )
            ),
            BibleReadingItem(
                id = 3,
                date = LocalDate.of(2000, Month.JANUARY, 3),
                isMarkedComplete = true,
                isToday = false,
                readings = listOf(
                    Reading.WholeBook(BibleBook.JUDGES),
                    Reading.WholeBook(BibleBook.RUTH),
                    Reading.WholeBook(BibleBook.PSALMS),
                )
            ),
            BibleReadingItem(
                id = 4,
                date = LocalDate.of(2000, Month.JANUARY, 4),
                isMarkedComplete = false,
                isToday = true,
                readings = listOf(
                    Reading.WholeBook(BibleBook.MATTHEW),
                    Reading.WholeBook(BibleBook.MARK),
                    Reading.WholeBook(BibleBook.LUKE),
                )
            ),
            BibleReadingItem(
                id = 5,
                date = LocalDate.of(2000, Month.JANUARY, 5),
                isMarkedComplete = false,
                isToday = false,
                readings = listOf(
                    Reading.WholeBook(BibleBook.JOHN),
                    Reading.WholeBook(BibleBook.ACTS),
                    Reading.WholeBook(BibleBook.ROMANS),
                )
            ),
        )

        // When
        subscribeToBibleReadings().test {
            val actual = awaitItem()

            Assert.assertEquals(expected, actual)

            awaitComplete()
        }
    }

    // Updates to Start date updates the result
    @Test
    fun `updates to start date updates the result`() = runTest {
        // Given
        val getCurrentDate = object : GetCurrentDate {
            override fun invoke(): LocalDate = LocalDate.of(2000, Month.JANUARY, 2)
        }
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> {
                return listOf(
                    BibleReadingDay(id = 1, offset = 0, readings = emptyList()),
                    BibleReadingDay(id = 2, offset = 1, readings = emptyList()),
                    BibleReadingDay(id = 3, offset = 2, readings = emptyList()),
                )
            }
            override suspend fun getReading(readingId: Int): BibleReadingDay? =
                TODO("Not Implemented")
        }
        val settingsRepository = object : SettingsRepository {
            override fun observeStartDate(): Flow<LocalDate> = flow {
                emit(LocalDate.of(2000, Month.JANUARY, 1))
                emit(LocalDate.of(2000, Month.JANUARY, 2))
                emit(LocalDate.of(2000, Month.JANUARY, 3))
                emit(LocalDate.of(2020, Month.MARCH, 7))
            }.onEach { delay(1) }

            override suspend fun setStartDate(date: LocalDate) = TODO("Not Implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not Implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not Implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not Implemented")
            override suspend fun getTheme(): Theme = TODO("Not Implemented")
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow { emit(1) }
            override suspend fun getLastReadOffset(): Int? = TODO("Not Implemented")
            override suspend fun setLastReadOffset(lastReadOffset: Int?) = TODO("Not Implemented")
        }
        val subscribeToBibleReadings = SubscribeToBibleReadingsUseCase(
            getCurrentDate = getCurrentDate,
            bibleDataRepository = bibleDataRepository,
            settingsRepository = settingsRepository,
            readInformationRepository = readInformationRepository,
        )


        // When
        subscribeToBibleReadings().test {
            val actualFirst = awaitItem()
            val expectedFirst = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = true, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualFirst, expectedFirst)

            val actualSecond = awaitItem()
            val expectedSecond = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = true, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 4), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualSecond, expectedSecond)

            val actualThird = awaitItem()
            val expectedThird = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 4), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 5), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualThird, expectedThird)

            val actualSeventh = awaitItem()
            val expectedSeventh = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2020, Month.MARCH, 7), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2020, Month.MARCH, 8), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2020, Month.MARCH, 9), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualSeventh, expectedSeventh)


            awaitComplete()
        }
    }

    @Test
    fun `updates to last read offset updates results`() = runTest {
        // Given
        val getCurrentDate = object : GetCurrentDate {
            override fun invoke(): LocalDate = LocalDate.of(2000, Month.JANUARY, 2)
        }
        val bibleDataRepository = object : BibleDataRepository {
            override suspend fun getBibleReadings(): List<BibleReadingDay> {
                return listOf(
                    BibleReadingDay(id = 1, offset = 0, readings = emptyList()),
                    BibleReadingDay(id = 2, offset = 1, readings = emptyList()),
                    BibleReadingDay(id = 3, offset = 2, readings = emptyList()),
                )
            }
            override suspend fun getReading(readingId: Int): BibleReadingDay? =
                TODO("Not Implemented")
        }
        val settingsRepository = object : SettingsRepository {
            override fun observeStartDate(): Flow<LocalDate> = flow {
                emit(LocalDate.of(2000, Month.JANUARY, 1))
            }

            override suspend fun setStartDate(date: LocalDate) = TODO("Not Implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not Implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not Implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not Implemented")
            override suspend fun getTheme(): Theme = TODO("Not Implemented")
        }
        val readInformationRepository = object : ReadInformationRepository {
            override fun subscribeGetLastReadOffset(): Flow<Int?> = flow {
                emit(null)
                emit(0)
                emit(1)
                emit(2)
                emit(3)
            }.onEach { delay(1) }
            override suspend fun getLastReadOffset(): Int? = TODO("Not Implemented")
            override suspend fun setLastReadOffset(lastReadOffset: Int?) = TODO("Not Implemented")
        }
        val subscribeToBibleReadings = SubscribeToBibleReadingsUseCase(
            getCurrentDate = getCurrentDate,
            bibleDataRepository = bibleDataRepository,
            settingsRepository = settingsRepository,
            readInformationRepository = readInformationRepository,
        )


        // When
        subscribeToBibleReadings().test {
            val actualNull = awaitItem()
            val expectedNull = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = false, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = false, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualNull, expectedNull)

            val actualZero = awaitItem()
            val expectedZero = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = false, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualZero, expectedZero)

            val actualOne = awaitItem()
            val expectedOne = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = true, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = false, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualOne, expectedOne)

            val actualTwo = awaitItem()
            val expectedTwo = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = true, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = true, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualTwo, expectedTwo)

            val actualThree = awaitItem()
            val expectedThree = listOf(
                BibleReadingItem(id = 1, date = LocalDate.of(2000, Month.JANUARY, 1), isMarkedComplete = true, isToday = false, readings = emptyList()),
                BibleReadingItem(id = 2, date = LocalDate.of(2000, Month.JANUARY, 2), isMarkedComplete = true, isToday = true, readings = emptyList()),
                BibleReadingItem(id = 3, date = LocalDate.of(2000, Month.JANUARY, 3), isMarkedComplete = true, isToday = false, readings = emptyList()),
            )

            Assert.assertEquals(actualThree, expectedThree)

            awaitComplete()
        }
    }
}
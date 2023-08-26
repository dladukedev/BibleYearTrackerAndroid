package com.dladukedev.bibleyeartracker.bibleReading.display

import app.cash.turbine.test
import com.dladukedev.bibleyeartracker.MainDispatcherRule
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingItem
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingComplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingIncomplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToBibleReadings
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToPercentComplete
import com.dladukedev.bibleyeartracker.common.DateFormat
import com.dladukedev.bibleyeartracker.common.FormatDate
import com.dladukedev.bibleyeartracker.common.GenerateID
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class BibleReadingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val subscribeToBibleReadings = object : SubscribeToBibleReadings {
        override fun invoke(): Flow<List<BibleReadingItem>> = flowOf(
            listOf(
                BibleReadingItem(1, LocalDate.of(2000, 1, 1), emptyList(), true, false),
                BibleReadingItem(2, LocalDate.of(2000, 1, 2), emptyList(), false, false),
                BibleReadingItem(3, LocalDate.of(2000, 1, 3), emptyList(), false, true),
            )
        )
    }

    private val subscribeToPercentComplete = object : SubscribeToPercentComplete {
        override fun invoke(): Flow<Double> = flowOf(33.333333)
    }

    private val markBibleReadingComplete = object : MarkBibleReadingComplete {
        override suspend fun invoke(bibleReadingId: Int): Boolean = true

    }

    private val markBibleReadingIncomplete = object : MarkBibleReadingIncomplete {
        override suspend fun invoke(bibleReadingId: Int): Boolean = true
    }

    private val formatDate = object : FormatDate {
        override fun invoke(date: LocalDate, format: DateFormat): String = "FORMATTED_DATE"
    }

    private val generateID = object : GenerateID {
        override fun invoke(): String = "GENERATED_ID"
    }

    @Test
    fun `uiState maps correct values on success`() = runTest {
        // Given
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val expected = BibleReadingViewModel.UIState.Content(
            readingItems = persistentListOf(
                BibleReadingItemDisplayModel(
                    1, "FORMATTED_DATE", persistentListOf(), true, false
                ),
                BibleReadingItemDisplayModel(
                    2, "FORMATTED_DATE", persistentListOf(), false, false
                ),
                BibleReadingItemDisplayModel(
                    3, "FORMATTED_DATE", persistentListOf(), false, true
                ),
            ),
            percentComplete = "33.33"
        )

        // When
        vm.uiState.test {
            val actual = awaitItem()

            // Then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `uiState emits Error if values fail`() = runTest {
        // Given
        val subscribeToBibleReadingsError = object : SubscribeToBibleReadings {
            override fun invoke(): Flow<List<BibleReadingItem>> = flow {
                throw Exception("Error")
            }
        }
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadingsError,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val expected = BibleReadingViewModel.UIState.Error

        // When
        vm.uiState.test {
            val actual = awaitItem()

            // Then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `uiState emits bible reading values over time`() = runTest {
        // Given
        val bibleReadingsOverTime = object : SubscribeToBibleReadings {
            override fun invoke(): Flow<List<BibleReadingItem>> = flowOf(
                listOf(
                    BibleReadingItem(1, LocalDate.of(2000, 1, 1), emptyList(), true, false),
                    BibleReadingItem(2, LocalDate.of(2000, 1, 2), emptyList(), false, false),
                    BibleReadingItem(3, LocalDate.of(2000, 1, 3), emptyList(), false, true),
                ),
                listOf(
                    BibleReadingItem(1, LocalDate.of(2000, 1, 1), emptyList(), true, false),
                    BibleReadingItem(2, LocalDate.of(2000, 1, 2), emptyList(), true, false),
                    BibleReadingItem(3, LocalDate.of(2000, 1, 3), emptyList(), false, true),
                ),
                listOf(
                    BibleReadingItem(1, LocalDate.of(2000, 1, 1), emptyList(), true, false),
                    BibleReadingItem(2, LocalDate.of(2000, 1, 2), emptyList(), false, false),
                    BibleReadingItem(3, LocalDate.of(2000, 1, 3), emptyList(), false, true),
                ),
            ).onEach { delay(1) }
        }

        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = bibleReadingsOverTime,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )


        // When
        vm.uiState.test {
            awaitItem() // StateFlow clear first

            // Then
            val actual1 = awaitItem()
            val expected1 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), false, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "33.33",
            )
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "33.33",
            )
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), false, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "33.33",
            )
            Assert.assertEquals(expected3, actual3)
        }
    }

    @Test
    fun `uiState emits percent values over time`() = runTest {
        // Given
        val percentCompleteOverTime = object : SubscribeToPercentComplete {
            override fun invoke(): Flow<Double> = flowOf(33.33333, 66.66666, 100.0).onEach { delay(1) }
        }

        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = percentCompleteOverTime,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )


        // When
        vm.uiState.test {
            awaitItem() // StateFlow clear first

            // Then
            val actual1 = awaitItem()
            val expected1 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), false, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "33.33"
            )
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), false, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "66.67"
            )
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = BibleReadingViewModel.UIState.Content(
                readingItems = persistentListOf(
                    BibleReadingItemDisplayModel(
                        1, "FORMATTED_DATE", persistentListOf(), true, false
                    ),
                    BibleReadingItemDisplayModel(
                        2, "FORMATTED_DATE", persistentListOf(), false, false
                    ),
                    BibleReadingItemDisplayModel(
                        3, "FORMATTED_DATE", persistentListOf(), false, true
                    ),
                ),
                percentComplete = "100.00"
            )
            Assert.assertEquals(expected3, actual3)
        }
    }

    @Test
    fun `markEventHandled removes value from events flow`() = runTest {
        // Given
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.SettingsRequested
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.requestViewSettings()
            awaitItem() // Item Sent

            vm.markEventHandled(event)

            // Then
            Assert.assertEquals(awaitItem(), emptyList<BibleReadingViewModel.Event>())
        }
    }

    @Test
    fun `markReadingComplete emits MarkReadSuccess event when successful`() = runTest {
        // Given
        val readingId = 1
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.MarkReadSuccess(readingId)
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.markReadingComplete(readingId)

            // Then
            Assert.assertEquals(awaitItem().first(), event)
        }
    }

    @Test
    fun `markReadingComplete emits MarkReadFailure event when failure`() = runTest {
        // Given
        val markReadCompleteFails = object : MarkBibleReadingComplete {
            override suspend fun invoke(bibleReadingId: Int): Boolean = false
        }
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markReadCompleteFails,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.MarkReadFailure
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.markReadingComplete(1)

            // Then
            Assert.assertEquals(awaitItem().first(), event)
        }
    }

    @Test
    fun `markReadingIncomplete emits UndoMarkReadSuccess event when successful`() = runTest {
        // Given
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.UndoMarkReadSuccess
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.undoMarkReadingComplete(1)

            // Then
            Assert.assertEquals(awaitItem().first(), event)
        }
    }

    @Test
    fun `markReadingIncomplete emits UndoMarkReadFailure event when failure`() = runTest {
        // Given
        val markReadIncompleteFails = object : MarkBibleReadingIncomplete {
            override suspend fun invoke(bibleReadingId: Int): Boolean = false
        }
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markReadIncompleteFails,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.UndoMarkReadFailure
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.undoMarkReadingComplete(1)

            // Then
            Assert.assertEquals(awaitItem().first(), event)
        }
    }

    @Test
    fun `requestViewSettings emits SettingsRequested event when called`() = runTest {
        // Given
        val vm = BibleReadingViewModel(
            subscribeToBibleReadings = subscribeToBibleReadings,
            subscribeToPercentComplete = subscribeToPercentComplete,
            markBibleReadingComplete = markBibleReadingComplete,
            markBibleReadingIncomplete = markBibleReadingIncomplete,
            formatDate = formatDate,
            generateID = generateID,
        )
        val event = BibleReadingViewModel.Event.SettingsRequested
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.requestViewSettings()

            // Then
            Assert.assertEquals(awaitItem().first(), event)
        }
    }
}
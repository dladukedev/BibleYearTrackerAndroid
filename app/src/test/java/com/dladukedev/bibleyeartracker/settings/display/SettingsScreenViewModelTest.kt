package com.dladukedev.bibleyeartracker.settings.display

import app.cash.turbine.test
import com.dladukedev.bibleyeartracker.MainDispatcherRule
import com.dladukedev.bibleyeartracker.bibleReading.domain.ResetReadingProgress
import com.dladukedev.bibleyeartracker.common.DateFormat
import com.dladukedev.bibleyeartracker.common.FormatDate
import com.dladukedev.bibleyeartracker.common.GenerateID
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.EditStartDate
import com.dladukedev.bibleyeartracker.settings.domain.EditTheme
import com.dladukedev.bibleyeartracker.settings.domain.ObserveSettings
import com.dladukedev.bibleyeartracker.settings.domain.Theme
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
import java.time.Month

class SettingsScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val observeSettings = object : ObserveSettings {
        override fun invoke(): Flow<AppSettings> = flowOf(
            AppSettings(readingGoalStartDate = LocalDate.of(2000, 1, 1), theme = Theme.SYSTEM),
        )
    }

    private val formatDate = object : FormatDate {
        override fun invoke(date: LocalDate, format: DateFormat): String = "FORMATTED_DATE"
    }

    private val editStartDate = object : EditStartDate {
        override suspend fun invoke(startDate: LocalDate) {}
    }

    private val editTheme = object : EditTheme {
        override suspend fun invoke(theme: Theme) {}
    }

    private val resetReadProgress = object : ResetReadingProgress {
        override suspend fun invoke() {}
    }

    private val generateID = object : GenerateID {
        override fun invoke(): String = "GENERATED_ID"
    }

    @Test
    fun `uiState maps correct values on success`() = runTest {
        // Given
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = SettingsScreenViewModel.UIState.Content(
            startDate = LocalDate.of(2000, 1, 1),
            startDateFormatted = "FORMATTED_DATE",
            theme = Theme.SYSTEM,
        )

        // When
        vm.uiState.test {
            val actual = awaitItem()

            // Then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `uiState emits Error is values fail`() = runTest {
        // Given
        val observeSettingsError = object : ObserveSettings {
            override fun invoke(): Flow<AppSettings> = flow {
                throw Exception("Error")
            }
        }
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettingsError,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = SettingsScreenViewModel.UIState.Error

        // When
        vm.uiState.test {
            val actual = awaitItem()

            // Then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `uiState emits settings values over time`() = runTest {
       // Given
        val observeSettingsOverTime = object : ObserveSettings {
            override fun invoke(): Flow<AppSettings> = flowOf(
                AppSettings(readingGoalStartDate = LocalDate.of(2000, 1, 1), theme = Theme.DARK),
                AppSettings(readingGoalStartDate = LocalDate.of(2000, 4, 17), theme = Theme.SYSTEM),
                AppSettings(readingGoalStartDate = LocalDate.of(2000, 1, 2), theme = Theme.LIGHT),
            ).onEach { delay(1) }
        }

        val vm = SettingsScreenViewModel(
            observeSettings = observeSettingsOverTime,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )

        // When
        vm.uiState.test {
            awaitItem() // StateFlow clear first

            // Then
            val actual1 = awaitItem()
            val expected1 = SettingsScreenViewModel.UIState.Content(
                startDate = LocalDate.of(2000, 1, 1),
                startDateFormatted = "FORMATTED_DATE",
                theme = Theme.DARK,
            )
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = SettingsScreenViewModel.UIState.Content(
                startDate = LocalDate.of(2000, 4, 17),
                startDateFormatted = "FORMATTED_DATE",
                theme = Theme.SYSTEM,
            )
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = SettingsScreenViewModel.UIState.Content(
                startDate = LocalDate.of(2000, 1, 2),
                startDateFormatted = "FORMATTED_DATE",
                theme = Theme.LIGHT,
            )
            Assert.assertEquals(expected3, actual3)
        }
    }

    @Test
    fun `updateTheme updates the theme`() = runTest {
        // Given
        val editTheme = object : EditTheme {
            var currentTheme: Theme? = null
            override suspend fun invoke(theme: Theme) {
               currentTheme = theme
            }
        }
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = Theme.LIGHT

        // When
        vm.updateTheme(Theme.LIGHT)
        val actual = editTheme.currentTheme

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `updateStartDate updates the startDate`() = runTest {
        // Given
        val editStartDate = object : EditStartDate {
            var currentDate: LocalDate? = null
            override suspend fun invoke(startDate: LocalDate) {
                currentDate = startDate
            }
        }
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = LocalDate.of(2000, Month.JANUARY, 1)

        // When
        vm.updateStartDate(LocalDate.of(2000, Month.JANUARY, 1))
        val actual = editStartDate.currentDate

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `reset calls reset`() = runTest {
        // Given
        val resetReadingProgress = object : ResetReadingProgress {
            var wasReset = false
            override suspend fun invoke() {
                wasReset = true
            }
        }
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadingProgress,
            generateID = generateID,
        )
        val expected = true

        // When
        vm.resetProgress()
        val actual = resetReadingProgress.wasReset

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `reset emits ProgressReset`() = runTest {
        // Given
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = SettingsScreenViewModel.Event.ProgressReset

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.resetProgress()
            val actual = awaitItem().first()

            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `markEventHandled removes value from events flow`() = runTest {
        // Given
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val event = SettingsScreenViewModel.Event.GoBackRequested
        event.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.requestGoBack()
            awaitItem() // Item Sent

            vm.markEventHandled(event)

            // Then
            Assert.assertEquals(awaitItem(), emptyList<SettingsScreenViewModel.Event>())
        }
    }

    @Test
    fun `requestGoBack emits GoBackRequested event`() = runTest{
        // Given
        val vm = SettingsScreenViewModel(
            observeSettings = observeSettings,
            formatDate = formatDate,
            editStartDate = editStartDate,
            editTheme = editTheme,
            resetReadingProgress = resetReadProgress,
            generateID = generateID,
        )
        val expected = SettingsScreenViewModel.Event.GoBackRequested
        expected.id = "GENERATED_ID"

        // When
        vm.events.test {
            awaitItem() // StateFlow clear first

            vm.requestGoBack()
            val actual = awaitItem().first()

            Assert.assertEquals(expected, actual)
        }
    }

}
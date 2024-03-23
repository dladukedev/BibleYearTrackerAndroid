package com.dladukedev.bibleyeartracker.settings.display

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import com.dladukedev.common.models.Theme
import kotlinx.collections.immutable.persistentListOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.Month


@OptIn(ExperimentalMaterial3Api::class)
class SettingsScreenStateHolderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialContentState() {
        composeTestRule.setContent {
            val state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = {},
            )

            Assert.assertTrue(state is com.dladukedev.feature.settings.display.SettingScreenState.Content)
            state as com.dladukedev.feature.settings.display.SettingScreenState.Content

            Assert.assertFalse(state.startDatePickerModalState.isShown)
            Assert.assertFalse(state.themePickerDialogState.isShown)
            Assert.assertEquals(com.dladukedev.common.models.Theme.SYSTEM, state.themePickerState.selectedTheme)
            Assert.assertFalse(state.resetProgressDialogState.isShown)
        }
    }


    @Test
    fun initialLoadingState() {
        composeTestRule.setContent {
            val state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Loading,
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = {},
            )

            Assert.assertTrue(state is com.dladukedev.feature.settings.display.SettingScreenState.Loading)
        }
    }

    @Test
    fun initialErrorState() {
        composeTestRule.setContent {
            val state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Error,
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = {},
            )

            Assert.assertTrue(state is com.dladukedev.feature.settings.display.SettingScreenState.Error)
        }
    }

    @Test
    fun goBackEvent() {
        var goBackCalledCount = 0
        var handledEvent: com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event? = null
        composeTestRule.setContent {
            com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event.GoBackRequested),
                goBack = { goBackCalledCount++ },
                requestGoBack = {},
                markEventHandled = { handledEvent = it },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = {},
            ) as com.dladukedev.feature.settings.display.SettingScreenState.Content
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(1, goBackCalledCount)
            Assert.assertEquals(handledEvent, com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event.GoBackRequested)
        }
    }

    @Test
    fun progressResetEvent() {
        var handledEvent: com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event? = null
        lateinit var state: com.dladukedev.feature.settings.display.SettingScreenState.Content
        composeTestRule.setContent {
            state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event.ProgressReset),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { handledEvent = it },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = {},
            ) as com.dladukedev.feature.settings.display.SettingScreenState.Content
        }


        composeTestRule.runOnIdle {
            Assert.assertEquals(handledEvent, com.dladukedev.feature.settings.display.SettingsScreenViewModel.Event.ProgressReset)
            Assert.assertNotNull(state.snackbarHostState.currentSnackbarData)
        }
    }

    @Test
    fun themeSelectDialogFlows() {
        var updateThemeCalledCount = 0
        lateinit var state: com.dladukedev.feature.settings.display.SettingScreenState.Content
        composeTestRule.setContent {
            state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = {},
                updateTheme = { updateThemeCalledCount++ },
                resetProgress = {},
            ) as com.dladukedev.feature.settings.display.SettingScreenState.Content
        }


        // Open Modal
        state.themePickerDialogState.showModal()

        composeTestRule.runOnIdle {
            Assert.assertTrue(state.themePickerDialogState.isShown)
        }

        // Pick Dark Mode
        state.themePickerState.onThemeSelected(com.dladukedev.common.models.Theme.DARK)

        composeTestRule.runOnIdle {
            Assert.assertEquals(com.dladukedev.common.models.Theme.DARK, state.themePickerState.selectedTheme)
        }


        composeTestRule.runOnIdle {
            Assert.assertEquals(0, updateThemeCalledCount)
        }

        // Submit Change
        state.themePickerDialogState.confirmModal()


        composeTestRule.runOnIdle {
            Assert.assertFalse(state.themePickerDialogState.isShown)
            Assert.assertEquals(1, updateThemeCalledCount)
        }

        // Open Modal and Select Light Mode
        state.themePickerDialogState.showModal()
        state.themePickerState.onThemeSelected(com.dladukedev.common.models.Theme.LIGHT)
        composeTestRule.runOnIdle {
            Assert.assertEquals(com.dladukedev.common.models.Theme.LIGHT, state.themePickerState.selectedTheme)
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(1, updateThemeCalledCount)
        }

        // Cancel Change
        state.themePickerDialogState.dismissModal()

        composeTestRule.runOnIdle {
            Assert.assertFalse(state.themePickerDialogState.isShown)
            Assert.assertEquals(1, updateThemeCalledCount)
        }

        // Reopen Modal
        state.themePickerDialogState.showModal()

        composeTestRule.runOnIdle {
            Assert.assertEquals(com.dladukedev.common.models.Theme.SYSTEM, state.themePickerState.selectedTheme)
        }
    }

    @Test
    fun resetProgressDialogFlows() {
        var resetProgressCalled = 0
        lateinit var state: com.dladukedev.feature.settings.display.SettingScreenState.Content
        composeTestRule.setContent {
            state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = {},
                updateTheme = {},
                resetProgress = { resetProgressCalled++ },
            ) as com.dladukedev.feature.settings.display.SettingScreenState.Content
        }

        // Open Modal
        state.resetProgressDialogState.showModal()

        composeTestRule.runOnIdle {
            Assert.assertTrue(state.resetProgressDialogState.isShown)
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(0, resetProgressCalled)
        }

        // Dismiss Modal
        state.resetProgressDialogState.dismissModal()

        composeTestRule.runOnIdle {
            Assert.assertFalse(state.resetProgressDialogState.isShown)
            Assert.assertEquals(0, resetProgressCalled)
        }

        // Open Modal
        state.resetProgressDialogState.showModal()

        // Confirm Modal
        state.resetProgressDialogState.confirmModal()

        composeTestRule.runOnIdle {
            Assert.assertFalse(state.resetProgressDialogState.isShown)
            Assert.assertEquals(1, resetProgressCalled)
        }
    }


    @Test
    fun startDatePickerModalFlows() {
        var updateStartDateCallCount = 0
        var updatedDate: LocalDate? = null
        lateinit var state: com.dladukedev.feature.settings.display.SettingScreenState.Content
        composeTestRule.setContent {
            state = com.dladukedev.feature.settings.display.rememberSettingScreenStateHolder(
                state = com.dladukedev.feature.settings.display.SettingsScreenViewModel.UIState.Content(
                    startDate = LocalDate.of(2000, Month.JANUARY, 1),
                    startDateFormatted = "Jan 01",
                    theme = com.dladukedev.common.models.Theme.SYSTEM,
                ),
                events = persistentListOf(),
                goBack = {},
                requestGoBack = {},
                markEventHandled = { _ -> },
                updateStartDate = { updateStartDateCallCount++; updatedDate = it },
                updateTheme = {},
                resetProgress = {},
            ) as com.dladukedev.feature.settings.display.SettingScreenState.Content
        }

        // Open Modal
        state.startDatePickerModalState.showModal()

        composeTestRule.runOnIdle {
            Assert.assertTrue(state.startDatePickerModalState.isShown)
            val jan_1_2000_Millis = 946684800000
            Assert.assertEquals(jan_1_2000_Millis, state.datePickerState.selectedDateMillis)
        }

        // Pick Date
        state.datePickerState.selectedDateMillis = 952664400000

        // Confirm
        state.startDatePickerModalState.confirmModal()

        composeTestRule.runOnIdle {
            Assert.assertFalse(state.startDatePickerModalState.isShown)
            Assert.assertEquals(1, updateStartDateCallCount)
            Assert.assertEquals(LocalDate.of(2000, Month.MARCH, 10), updatedDate)
        }

        // Reset
        updatedDate = null

        // Open Modal and Select Date
        state.startDatePickerModalState.showModal()
        state.datePickerState.selectedDateMillis = 959918400000

        // Dismiss
        state.startDatePickerModalState.dismissModal()

        composeTestRule.runOnIdle {
            Assert.assertFalse(state.startDatePickerModalState.isShown)
            Assert.assertEquals(1, updateStartDateCallCount)
            Assert.assertNull(updatedDate)
        }
    }
}
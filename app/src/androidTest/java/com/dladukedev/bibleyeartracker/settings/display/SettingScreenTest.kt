package com.dladukedev.bibleyeartracker.settings.display

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.dladukedev.bibleyeartracker.app.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SettingScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()

        // Go To Settings Screen To Start
        composeTestRule.onNodeWithContentDescription("View Settings").performClick()
    }

    @Test
    fun displayScreen() {
        // Heading
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Go Back").assertIsDisplayed()

        // Goals
        composeTestRule.onNodeWithText("Goals").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start Date").assertIsDisplayed()

        // Appearance
        composeTestRule.onNodeWithText("Appearance").assertIsDisplayed()
        composeTestRule.onNodeWithText("Theme").assertIsDisplayed()
        composeTestRule.onNodeWithText("System Default").assertIsDisplayed()

        // Advanced
        composeTestRule.onNodeWithText("Advanced").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reset Progress").assertIsDisplayed()
    }

    @Test
    fun goBack() {
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Go Back").performClick()

        composeTestRule.onNodeWithText("0.00% Complete").assertIsDisplayed()
    }

    @Test
    fun displayChangeStartDateDialog() {
        composeTestRule.onNodeWithText("Start Date").performClick()

        composeTestRule.onNode(isDialog()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Select date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Update").assertIsDisplayed()
    }

    @Test
    fun changeStartDate() {
        composeTestRule.onNodeWithText("Start Date").performClick()

        composeTestRule.onNodeWithContentDescription("Switch to text input mode").performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextClearance()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("01012020")
        composeTestRule.onNodeWithText("Update").performClick()

        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("(Today)", substring = true).assertDoesNotExist()
    }

    @Test
    fun cancelChangeStartDate() {
        composeTestRule.onNodeWithText("Start Date").performClick()

        composeTestRule.onNodeWithContentDescription("Switch to text input mode").performClick()
        composeTestRule.onNode(hasSetTextAction()).performTextClearance()
        composeTestRule.onNode(hasSetTextAction()).performTextInput("01012020")
        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("(Today)", substring = true).assertIsDisplayed()
    }

    @Test
    fun displayChangeThemeDialog() {
        composeTestRule.onNodeWithText("Theme").performClick()

        composeTestRule.onNode(isDialog()).assertIsDisplayed()

        composeTestRule.onNodeWithText("Choose theme").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNodeWithText("Update").assertIsDisplayed()

        composeTestRule.onNodeWithText("Light").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dark").assertIsDisplayed()
        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("System Default"))).assertIsDisplayed()
    }

    @Test
    fun changeTheme() {
        composeTestRule.onNodeWithText("System Default").assertIsDisplayed()
        composeTestRule.onNodeWithText("Light").assertDoesNotExist()

        composeTestRule.onNodeWithText("Theme").performClick()

        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("System Default"))).assertIsSelected()

        composeTestRule.onNodeWithText("Light").performClick()
        composeTestRule.onNodeWithText("Light").assertIsSelected()

        composeTestRule.onNodeWithText("Update").performClick()

        composeTestRule.onNodeWithText("System Default").assertDoesNotExist()
        composeTestRule.onNodeWithText("Light").assertIsDisplayed()

        // Undo
        composeTestRule.onNodeWithText("Theme").performClick()
        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("System Default"))).performClick()
        composeTestRule.onNodeWithText("Update").performClick()
    }

    @Test
    fun cancelChangeTheme() {
        composeTestRule.onNodeWithText("System Default").assertIsDisplayed()

        composeTestRule.onNodeWithText("Theme").performClick()

        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("System Default"))).assertIsSelected()

        composeTestRule.onNodeWithText("Light").performClick()
        composeTestRule.onNodeWithText("Light").assertIsSelected()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithText("System Default").assertIsDisplayed()
        composeTestRule.onNodeWithText("Light").assertDoesNotExist()
    }

    @Test
    fun displayResetProgressDialog() {
        composeTestRule.onNodeWithText("Reset Progress").performClick()

        composeTestRule.onNode(isDialog()).assertIsDisplayed()
        composeTestRule.onNodeWithText("Are you sure you want to reset your progress?").assertIsDisplayed()
        composeTestRule.onNodeWithText("This action cannot be undone.").assertIsDisplayed()

        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("Reset Progress"))).assertIsDisplayed()
    }

    @Test
    fun resetProgress() {
        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("Complete").performClick()
        composeTestRule.onNodeWithText("0.27% Complete").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("View Settings").performClick()
        composeTestRule.onNodeWithText("Reset Progress").performClick()

        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("Reset Progress"))).performClick()
        composeTestRule.onNodeWithText("Your progress has been reset").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("0.00% Complete").assertIsDisplayed()
    }


    @Test
    fun cancelResetProgress() {
        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("Complete").performClick()
        composeTestRule.onNodeWithText("0.27% Complete").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("View Settings").performClick()
        composeTestRule.onNodeWithText("Reset Progress").performClick()

        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Your progress has been reset").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Go Back").performClick()
        composeTestRule.onNodeWithText("0.27% Complete").assertIsDisplayed()

        // Undo
        composeTestRule.onNodeWithContentDescription("View Settings").performClick()
        composeTestRule.onNodeWithText("Reset Progress").performClick()
        composeTestRule.onNode(hasAnyAncestor(isDialog()).and(hasText("Reset Progress"))).performClick()
    }
}
package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.dladukedev.bibleyeartracker.app.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class BibleReadingScreenTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun displayScreen() {
        // Header
        composeTestRule.onNodeWithText("0.00% Complete").assertIsDisplayed()

        // Settings Button
        composeTestRule.onNodeWithContentDescription("View Settings").assertIsDisplayed()

        // Current Reading Card
        composeTestRule.onNodeWithText("Complete").assertIsDisplayed()
        composeTestRule.onNodeWithText("(Today)", substring = true).assertIsDisplayed()

        // Upcoming Reading Card
        composeTestRule.onNodeWithText("Return to Top of List").assertDoesNotExist()

    }

    @Test
    fun scrollToTop() {
        composeTestRule.onNodeWithContentDescription("Return to Top of List").assertDoesNotExist()
        composeTestRule.onNodeWithText("Complete").assertIsDisplayed()

        // When
        composeTestRule.onNode(hasScrollAction()).performScrollToIndex(100)

        // Then
        composeTestRule.onNodeWithContentDescription("Return to Top of List").assertIsDisplayed()
        composeTestRule.onNodeWithText("Complete").assertIsNotDisplayed()

        // When
        composeTestRule.onNodeWithContentDescription("Return to Top of List").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Return to Top of List").assertDoesNotExist()
        composeTestRule.onNodeWithText("Complete").assertIsDisplayed()
    }

    @Test
    fun onUndoMarkComplete() {
        // When
        composeTestRule.onNodeWithText("(Today)", substring = true).assertIsDisplayed()

        composeTestRule.onNodeWithText("Complete").performClick()
        composeTestRule.onNodeWithText("Undo").performClick()

        // Then
        composeTestRule.onNodeWithText("The reading is once again marked unread").assertIsDisplayed()
        composeTestRule.onNodeWithText("(Today)", substring = true).assertIsDisplayed()
    }

    @Test
    fun onMarkComplete() {
        // When
        composeTestRule.onNodeWithText("(Today)", substring = true).assertIsDisplayed()

        composeTestRule.onNodeWithText("Complete").performClick()

        // Then
        composeTestRule.onNodeWithText("Great work! Another set of readings complete!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Undo").assertIsDisplayed()
        composeTestRule.onNodeWithText("0.27% Complete").assertIsDisplayed()
        composeTestRule.onNodeWithText("(Today)", substring = true).assertDoesNotExist()

        // Reset
        composeTestRule.onNodeWithText("Undo").performClick()
    }

    @Test
    fun navigateToSettings() {
        // When
        composeTestRule.onNodeWithContentDescription("View Settings").performClick()

        // Then
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun displayComplete() {
        // When
        repeat(365) {
            composeTestRule.onNodeWithText("Complete").performClick()
        }

        // Then
        composeTestRule.onNodeWithText("Congratulations!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Complete").assertDoesNotExist()

        // Undo
        composeTestRule.onNodeWithContentDescription("View Settings").performClick()
        composeTestRule.onNodeWithText("Reset Progress").performClick()
        composeTestRule.onNode(hasAnyAncestor(isDialog()) and hasText("Reset Progress")).performClick()
    }
}
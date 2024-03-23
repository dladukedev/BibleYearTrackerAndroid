package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.junit4.createComposeRule
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingItemDisplayModel
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingScreenState
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingScreenUIState
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingViewModel
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingsState
import com.dladukedev.feature.readings.readinglistscreen.rememberBibleReadingScreenStateHolder
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class BibleReadingScreenStateTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initialLoadingState() {
        composeTestRule.setContent {
            val state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Loading,
                events = persistentListOf(),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = {},
            )

            Assert.assertTrue(state.uiState is BibleReadingScreenUIState.Loading)
        }


    }

    @Test
    fun initialErrorState() {
        composeTestRule.setContent {
            val state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Error,
                events = persistentListOf(),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = {},
            )

            Assert.assertTrue(state.uiState is BibleReadingScreenUIState.Error)
        }
    }

    @Test
    fun initialContentComplete() {
        composeTestRule.setContent {
            val state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), true, true
                        ),
                    ),
                ),
                events = persistentListOf(),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = {},
            )

            Assert.assertTrue(state.uiState is BibleReadingScreenUIState.Content)
            Assert.assertTrue((state.uiState as BibleReadingScreenUIState.Content).readingsState is BibleReadingsState.Complete)
        }
    }

    @Test
    fun initialContentIncomplete() {
        composeTestRule.setContent {
            val state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = {},
            )

            Assert.assertTrue(state.uiState is BibleReadingScreenUIState.Content)
            Assert.assertTrue((state.uiState as BibleReadingScreenUIState.Content).readingsState is BibleReadingsState.InProgress)
        }
    }

    @Test
    fun scrollToTop() {
        lateinit var state: BibleReadingScreenState
        lateinit var coroutineScope: CoroutineScope
        composeTestRule.setContent {
            coroutineScope = rememberCoroutineScope()
            state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = (0..100).map {
                        BibleReadingItemDisplayModel(
                            it, "DATE", persistentListOf(), false, false
                        )
                    }.toPersistentList()
                ),
                events = persistentListOf(),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = {},
                coroutineScope = coroutineScope,
            )
        }


        coroutineScope.launch {
            state.lazyListState.scrollToItem(2)

            composeTestRule.runOnIdle {
                Assert.assertEquals(92, state.lazyListState.firstVisibleItemIndex)
                Assert.assertTrue(state.showButton)
            }

            state.scrollToTop()

            composeTestRule.runOnIdle {
                Assert.assertEquals(0, state.lazyListState.firstVisibleItemIndex)
                Assert.assertFalse(state.showButton)
            }
        }
    }

    @Test
    fun settingsRequestedEvent() {
        var handledEvent: BibleReadingViewModel.Event? = null
        var settingsClicked = 0
        composeTestRule.setContent {
            rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(BibleReadingViewModel.Event.SettingsRequested),
                onSettingsClick = { settingsClicked++ },
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = { handledEvent = it },
            )
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(1, settingsClicked)
            Assert.assertEquals(BibleReadingViewModel.Event.SettingsRequested, handledEvent)
        }
    }

    @Test
    fun markReadSuccessEvent() {
        lateinit var state: BibleReadingScreenState
        var handledEvent: BibleReadingViewModel.Event? = null
        var undoClicked = 0
        composeTestRule.setContent {
            state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(BibleReadingViewModel.Event.MarkReadSuccess(1)),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = { undoClicked++ },
                requestViewSettings = {},
                markEventHandled = { handledEvent = it },
            )
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(BibleReadingViewModel.Event.MarkReadSuccess(1), handledEvent)
            Assert.assertNotNull(state.snackbarHostState.currentSnackbarData)
        }

        state.snackbarHostState.currentSnackbarData!!.performAction()

        composeTestRule.runOnIdle {
            Assert.assertEquals(1, undoClicked)
        }
    }

    @Test
    fun markReadFailure() {
        lateinit var state: BibleReadingScreenState
        var handledEvent: BibleReadingViewModel.Event? = null
        composeTestRule.setContent {
            state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(BibleReadingViewModel.Event.MarkReadFailure),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = { handledEvent = it },
            )
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(BibleReadingViewModel.Event.MarkReadFailure, handledEvent)
            Assert.assertNotNull(state.snackbarHostState.currentSnackbarData)
        }
    }

    @Test
    fun undoMarkReadSuccess() {
        lateinit var state: BibleReadingScreenState
        var handledEvent: BibleReadingViewModel.Event? = null
        composeTestRule.setContent {
            state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(BibleReadingViewModel.Event.UndoMarkReadSuccess),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = { handledEvent = it },
            )
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(BibleReadingViewModel.Event.UndoMarkReadSuccess, handledEvent)
            Assert.assertNotNull(state.snackbarHostState.currentSnackbarData)
        }
    }

    @Test
    fun undoMarkReadFailure() {
        lateinit var state: BibleReadingScreenState
        var handledEvent: BibleReadingViewModel.Event? = null
        composeTestRule.setContent {
            state = rememberBibleReadingScreenStateHolder(
                state = BibleReadingViewModel.UIState.Content(
                    percentComplete = "100.00%",
                    readingItems = persistentListOf(
                        BibleReadingItemDisplayModel(
                            1, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            2, "DATE", persistentListOf(), true, false
                        ),
                        BibleReadingItemDisplayModel(
                            3, "DATE", persistentListOf(), false, true
                        ),
                    ),
                ),
                events = persistentListOf(BibleReadingViewModel.Event.UndoMarkReadFailure),
                onSettingsClick = {},
                onCompleteClick = {},
                onUndoClick = {},
                requestViewSettings = {},
                markEventHandled = { handledEvent = it },
            )
        }

        composeTestRule.runOnIdle {
            Assert.assertEquals(BibleReadingViewModel.Event.UndoMarkReadFailure, handledEvent)
            Assert.assertNotNull(state.snackbarHostState.currentSnackbarData)
        }
    }
}
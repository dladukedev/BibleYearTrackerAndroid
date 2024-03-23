package com.dladukedev.feature.readings.readinglistscreen

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.dladukedev.common.util.extensions.headTail
import com.dladukedev.feature.readings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
data class BibleReadingScreenState(
    val snackbarHostState: SnackbarHostState,
    val lazyListState: LazyListState,
    val scrollToTop: () -> Unit,
    val requestViewSettings: () -> Unit,
    val requestViewStatistics: () -> Unit,
    val onCompleteClick: () -> Unit,
    val uiState: BibleReadingScreenUIState,
    val showButton: Boolean,
)

sealed class BibleReadingScreenUIState {
    object Loading : BibleReadingScreenUIState()

    data class Content(
        val percentComplete: String,
        val readingsState: BibleReadingsState,
    ) : BibleReadingScreenUIState()

    object Error : BibleReadingScreenUIState()
}

sealed class BibleReadingsState {
    data class InProgress(
        val nextReading: BibleReadingItemDisplayModel,
        val completeReadings: ImmutableList<BibleReadingItemDisplayModel>,
        val incompleteReadings: ImmutableList<BibleReadingItemDisplayModel>
    ) : BibleReadingsState()

    data class Complete(
        val completeReadings: ImmutableList<BibleReadingItemDisplayModel>,
    ) : BibleReadingsState()
}

@Composable
fun rememberBibleReadingScreenStateHolder(
    state: BibleReadingViewModel.UIState,
    event: BibleReadingViewModel.Event?,
    onSettingsClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onUndoClick: () -> Unit,
    requestViewSettings: () -> Unit,
    requestViewStatistics: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    lazyListState: LazyListState = rememberLazyListState(),
): BibleReadingScreenState {
    val markCompleteSuccessSnackbarMessage =
        stringResource(id = R.string.reading_list_mark_complete_success_snackbar)
    val markCompleteSuccessSnackbarUndoAction =
        stringResource(id = R.string.reading_list_mark_complete_success_snackbar_undo_action)
    val markCompleteErrorSnackbarMessage =
        stringResource(id = R.string.reading_list_mark_complete_error_snackbar)
    val undoMarkCompleteSuccessSnackbarMessage =
        stringResource(id = R.string.reading_list_undo_mark_complete_success_snackbar)
    val undoMarkCompleteErrorSnackbarMessage =
        stringResource(id = R.string.reading_list_undo_mark_complete_error_snackbar)

    LaunchedEffect(event) {
        event ?: return@LaunchedEffect

        when (event) {
            BibleReadingViewModel.Event.MarkReadSuccess -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    if (snackbarHostState.showSnackbar(
                            markCompleteSuccessSnackbarMessage,
                            markCompleteSuccessSnackbarUndoAction,
                            duration = SnackbarDuration.Long,
                        ) == SnackbarResult.ActionPerformed
                    ) {
                        onUndoClick()
                    }
                }
            }

            BibleReadingViewModel.Event.MarkReadFailure -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        markCompleteErrorSnackbarMessage,
                    )
                }
            }

            BibleReadingViewModel.Event.SettingsRequested -> {
                onSettingsClick()
            }

            BibleReadingViewModel.Event.StatisticsRequested -> {
                onStatisticsClick()
            }

            BibleReadingViewModel.Event.UndoMarkReadFailure -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        undoMarkCompleteErrorSnackbarMessage,
                    )
                }

            }

            BibleReadingViewModel.Event.UndoMarkReadSuccess -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        undoMarkCompleteSuccessSnackbarMessage,
                    )
                }
            }
        }
    }

    val showButton by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    val uiState = when (state) {
        is BibleReadingViewModel.UIState.Content -> {
            val readings = state.readingItems
            val groupedReadings = remember(readings) {
                readings.groupBy { it.isMarkedComplete }.toImmutableMap()
            }
            val incompleteReadings = remember(groupedReadings) {
                groupedReadings.getOrDefault(false, emptyList()).toImmutableList()
            }

            val completeReadings = remember(groupedReadings) {
                groupedReadings.getOrDefault(true, emptyList()).toImmutableList()
            }

            val readingsState = when {
                incompleteReadings.isEmpty() -> BibleReadingsState.Complete(completeReadings)
                else -> {
                    val (nextReading, remainingReadings) = incompleteReadings.headTail()
                    BibleReadingsState.InProgress(
                        nextReading = nextReading,
                        completeReadings = completeReadings,
                        incompleteReadings = remainingReadings.toImmutableList(),
                    )
                }
            }
            BibleReadingScreenUIState.Content(state.percentComplete, readingsState)
        }

        BibleReadingViewModel.UIState.Error -> BibleReadingScreenUIState.Error
        BibleReadingViewModel.UIState.Loading -> BibleReadingScreenUIState.Loading
    }

    val scrollToTop = remember<() -> Unit> {
        {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(index = 0)
            }
        }
    }


    return BibleReadingScreenState(
        snackbarHostState = snackbarHostState,
        lazyListState = lazyListState,
        scrollToTop = scrollToTop,
        requestViewSettings = requestViewSettings,
        requestViewStatistics = requestViewStatistics,
        uiState = uiState,
        showButton = showButton,
        onCompleteClick = onCompleteClick,
    )
}
package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.app.theme.Spacing
import com.dladukedev.bibleyeartracker.common.display.ErrorComponent
import com.dladukedev.bibleyeartracker.common.display.LoadingComponent

@Composable
fun BibleReadingScreen(
    goToSettingScreen: () -> Unit,
    modifier: Modifier = Modifier,
    vm: BibleReadingViewModel = hiltViewModel(),
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val events by vm.events.collectAsStateWithLifecycle()

    BibleReadingScreen(
        state = rememberBibleReadingScreenStateHolder(
            state = state,
            events = events,
            onSettingsClick = goToSettingScreen,
            onCompleteClick = vm::markReadingComplete,
            onUndoClick = vm::undoMarkReadingComplete,
            requestViewSettings = vm::requestViewSettings,
            markEventHandled = vm::markEventHandled,
        ),
        modifier = modifier,
    )
}

@Composable
fun BibleReadingScreen(
    state: BibleReadingScreenState,
    modifier: Modifier = Modifier,
) {
    val returnToTopButtonContentDescription =
        stringResource(id = R.string.reading_list_return_to_top_button_content_description)

    Scaffold(
        snackbarHost = { SnackbarHost(state.snackbarHostState) },
        topBar = {
            if (state.uiState is BibleReadingScreenUIState.Content) {
                BibleReadingScreenAppBar(
                    uiState = state.uiState,
                    onViewSettingsClick = state.requestViewSettings
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = state.showButton,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                FloatingActionButton(
                    onClick = state.scrollToTop,
                    modifier = Modifier.semantics {
                        contentDescription = returnToTopButtonContentDescription
                    }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = null)
                }
            }
        },
        modifier = modifier,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when (val uiState = state.uiState) {
                is BibleReadingScreenUIState.Content -> BibleReadingScreenContent(
                    uiState,
                    state.lazyListState,
                    onCompleteClick = state.onCompleteClick,
                )

                BibleReadingScreenUIState.Error -> ErrorComponent()
                BibleReadingScreenUIState.Loading -> LoadingComponent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReadingScreenAppBar(
    uiState: BibleReadingScreenUIState.Content,
    onViewSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewSettingsContentDescription = stringResource(
        id = R.string.reading_list_settings_button_content_description
    )

    TopAppBar(
        title = { Text(stringResource(id = R.string.reading_list_title, uiState.percentComplete)) },
        actions = {
            IconButton(onClick = onViewSettingsClick, modifier = Modifier.semantics {
                contentDescription = viewSettingsContentDescription
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
fun BibleReadingScreenContent(
    data: BibleReadingScreenUIState.Content,
    lazyListState: LazyListState,
    onCompleteClick: (Int) -> Unit,
) {
    when (val screenState = data.readingsState) {
        is BibleReadingsState.InProgress -> BibleReadingScreenContentInProgress(
            readingsState = screenState,
            onCompleteClick = onCompleteClick,
            lazyListState = lazyListState,
        )

        is BibleReadingsState.Complete -> BibleReadingScreenContentComplete(
            readingsState = screenState,
            lazyListState = lazyListState,
        )
    }
}

@Composable
fun BibleReadingScreenContentInProgress(
    readingsState: BibleReadingsState.InProgress,
    onCompleteClick: (Int) -> Unit,
    lazyListState: LazyListState,
) {
    LazyColumn(state = lazyListState) {
        item {
            NextBibleReading(
                bibleReading = readingsState.nextReading,
                onCompleteClick = onCompleteClick,
                modifier = Modifier.padding(
                    horizontal = Spacing.MEDIUM
                )
            )
            Spacer(modifier = Modifier.height(Spacing.LARGE))
        }
        items(items = readingsState.incompleteReadings, key = { it.id }) { reading ->
            BibleReadingListItem(readingItem = reading)
        }
        if (readingsState.completeReadings.isNotEmpty()) {
            item {
                Divider(
                    modifier = Modifier.padding(
                        horizontal = Spacing.SMALL,
                        vertical = Spacing.X_LARGE,
                    )
                )
            }
        }
        items(items = readingsState.completeReadings, key = { it.id }) { reading ->
            BibleReadingListItem(readingItem = reading)
        }
    }
}

@Composable
fun BibleReadingScreenContentComplete(
    readingsState: BibleReadingsState.Complete,
    lazyListState: LazyListState
) {
    LazyColumn(state = lazyListState) {
        item {
            ReadingsCompleteItem(
                modifier = Modifier.padding(
                    horizontal = Spacing.MEDIUM
                )
            )
            Spacer(modifier = Modifier.height(Spacing.LARGE))
        }
        items(items = readingsState.completeReadings, key = { it.id }) { reading ->
            BibleReadingListItem(readingItem = reading)
        }
    }
}




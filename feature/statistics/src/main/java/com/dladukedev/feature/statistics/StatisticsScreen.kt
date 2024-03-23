package com.dladukedev.feature.statistics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dladukedev.common.ui.BibleBookNameLookup
import com.dladukedev.common.ui.components.ErrorComponent
import com.dladukedev.common.ui.components.LoadingComponent
import com.dladukedev.common.ui.values.Spacing
import com.dladukedev.core.statistics.ReadStatus
import com.dladukedev.core.statistics.Stats
import com.dladukedev.feature.statistics.components.StatisticsBibleBookReadStatusGroupTitle
import com.dladukedev.feature.statistics.components.StatisticsBibleBookReadStatusListItem
import com.dladukedev.feature.statistics.components.StatisticsBibleBookReadStatusListState
import com.dladukedev.feature.statistics.components.StatisticsHeader
import com.dladukedev.feature.statistics.components.StatisticsHeaderState
import com.dladukedev.feature.statistics.components.StatisticsOverviewCard
import com.dladukedev.feature.statistics.components.StatisticsOverviewCardState
import com.dladukedev.feature.statistics.components.previewStatisticsBibleBookReadStatusListState
import com.dladukedev.feature.statistics.components.previewStatisticsHeaderState
import com.dladukedev.feature.statistics.components.previewStatisticsOverviewCardState
import com.dladukedev.feature.statistics.components.rememberStatisticsBibleBookReadStatusListState
import com.dladukedev.feature.statistics.components.rememberStatisticsHeaderState
import com.dladukedev.feature.statistics.components.rememberStatisticsOverviewCardState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

@Composable
fun StatisticsScreen(
    goBack: () -> Unit,
    viewModel: StatisticsScreenViewModel = hiltViewModel()
) {
    val vmState by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle(null)

    val state = statisticsScreenStatHolder(
        vmState = vmState,
        event = event,
        goBack = goBack,
    )

    StatisticsScreen(state = state)
}

@Composable
private fun StatisticsScreen(state: StatisticsScreenState) {
    Scaffold(
        topBar = {
            StatisticsHeader(state = state.headerState)
        }
    ) { padding ->

        Box(Modifier.padding(padding)) {
            when(state.contentState) {
                is StatisticsScreenContentState.Content -> {
                    LazyColumn( contentPadding = PaddingValues(horizontal = Spacing.MEDIUM), modifier = Modifier.fillMaxWidth()) {
                        item {
                            StatisticsOverviewCard(
                                state = state.contentState.overviewCardState,
                                modifier = Modifier.fillMaxWidth())
                        }
                        item {
                            Spacer(modifier = Modifier.height(Spacing.LARGE))
                        }
                        item {
                            StatisticsBibleBookReadStatusGroupTitle(title = state.contentState.bibleBookReadStatusListState.oldTestamentTitle, modifier = Modifier.padding(vertical = Spacing.SMALL))
                        }
                        items(state.contentState.bibleBookReadStatusListState.oldTestamentItems, key = { it.bookName }) {bookStatusItemState ->
                            StatisticsBibleBookReadStatusListItem(state = bookStatusItemState)
                        }
                        item {
                            StatisticsBibleBookReadStatusGroupTitle(title = state.contentState.bibleBookReadStatusListState.newTestamentTitle, modifier = Modifier.padding(vertical = Spacing.SMALL))
                        }
                        items(state.contentState.bibleBookReadStatusListState.newTestamentItems, key = { it.bookName }) { bookStatusItemState ->
                            StatisticsBibleBookReadStatusListItem(state = bookStatusItemState)
                        }
                    }
                }
                StatisticsScreenContentState.Error -> ErrorComponent()
                StatisticsScreenContentState.Loading -> LoadingComponent()
            }
        }

    }
}


data class StatisticsScreenState(
    val headerState: StatisticsHeaderState,
    val contentState: StatisticsScreenContentState,
)

sealed class StatisticsScreenContentState {
    data class Content(
        val overviewCardState: StatisticsOverviewCardState,
        val bibleBookReadStatusListState: StatisticsBibleBookReadStatusListState,
    ): StatisticsScreenContentState ()
    data object Error: StatisticsScreenContentState()
    data object Loading: StatisticsScreenContentState()
}

@Composable
fun statisticsScreenStatHolder(
    vmState: StatisticsScreenViewModel.State,
    event: StatisticsScreenViewModel.Event?,
    goBack: () -> Unit,
): StatisticsScreenState {
    LaunchedEffect(event) {
        event ?: return@LaunchedEffect

        when(event) {
            StatisticsScreenViewModel.Event.goBack -> goBack()
        }
    }

    val headerState = rememberStatisticsHeaderState(onBackPressed = goBack)

    val contentState = when(vmState) {
        is StatisticsScreenViewModel.State.Content -> {
            val overviewCardState = rememberStatisticsOverviewCardState(stats = vmState.stats)
            val bibleBookReadStatusListState = rememberStatisticsBibleBookReadStatusListState(bookStatuses = vmState.bibleBooksReadState)

             StatisticsScreenContentState.Content(
                overviewCardState = overviewCardState,
                bibleBookReadStatusListState = bibleBookReadStatusListState,
            )
        }
        StatisticsScreenViewModel.State.Error -> StatisticsScreenContentState.Error
        StatisticsScreenViewModel.State.Loading -> StatisticsScreenContentState.Loading
    }

    return remember(
        headerState,
        contentState,
    ) {
        StatisticsScreenState(
            headerState = headerState,
            contentState = contentState,
        )
    }
}

private val previewStatisticsScreenState = StatisticsScreenState(
    headerState = previewStatisticsHeaderState,
    contentState = StatisticsScreenContentState.Content(
        overviewCardState = previewStatisticsOverviewCardState,
        bibleBookReadStatusListState = previewStatisticsBibleBookReadStatusListState,
    ),
)

@Preview
@Composable
private fun StatisticsScreenPreview() {
    StatisticsScreen(state = previewStatisticsScreenState)
}
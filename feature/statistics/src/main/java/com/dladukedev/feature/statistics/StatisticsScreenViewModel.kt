package com.dladukedev.feature.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dladukedev.common.util.extensions.shareInPolicy
import com.dladukedev.core.statistics.BibleBookReadState
import com.dladukedev.core.statistics.Stats
import com.dladukedev.core.statistics.SubscribeBibleBooksReadState
import com.dladukedev.core.statistics.SubscribeToStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatisticsScreenViewModel @Inject constructor(
    subscribeToStats: SubscribeToStats,
    subscribeBibleBooksReadState: SubscribeBibleBooksReadState,
): ViewModel() {
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val state = combine(
        subscribeToStats.values,
        subscribeBibleBooksReadState.values,
    ) {stats, bibleBooksReadState ->
        State.Content(stats, bibleBooksReadState.toImmutableList())
    }.catch { State.Error }
        .stateIn(viewModelScope, shareInPolicy, State.Loading)

    sealed class State {
        data object Loading: State()
        data object Error: State()
        data class Content(
            val stats: Stats,
            val bibleBooksReadState: ImmutableList<BibleBookReadState>,
        ): State()
    }

    sealed class Event {
        data object goBack: Event()
    }
}
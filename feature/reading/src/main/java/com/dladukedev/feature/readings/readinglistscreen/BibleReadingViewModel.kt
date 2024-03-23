package com.dladukedev.feature.readings.readinglistscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dladukedev.common.models.BibleReadingItem
import com.dladukedev.core.schedule.SubscribeToBibleReadings
import com.dladukedev.common.util.datetime.DateFormat
import com.dladukedev.common.util.datetime.FormatDate
import com.dladukedev.common.util.extensions.shareInPolicy
import com.dladukedev.core.schedule.MarkBibleReadingComplete
import com.dladukedev.core.schedule.MarkBibleReadingIncomplete
import com.dladukedev.core.statistics.SubscribeToStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BibleReadingViewModel @Inject constructor(
    subscribeToBibleReadings: SubscribeToBibleReadings,
    subscribeToStats: SubscribeToStats,
    private val markBibleReadingComplete: MarkBibleReadingComplete,
    private val markBibleReadingIncomplete: MarkBibleReadingIncomplete,
    private val formatDate: FormatDate,
) : ViewModel() {
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    private val bibleReadingsFlow = subscribeToBibleReadings.values.map { readings ->
        readings.map { bibleReadingItemAsDisplayMode(it) }.toPersistentList()
    }

    private val percentCompleteFlow = subscribeToStats.values.map { stats ->
        val percentComplete = stats.percentComplete
        String.format("%.2f", percentComplete)
    }

    val uiState: StateFlow<UIState> =
        combine(bibleReadingsFlow, percentCompleteFlow) { bibleReadings, title ->
            UIState.Content(title, bibleReadings) as UIState
        }.onStart {
            emit(UIState.Loading)
        }.catch {
            emit(UIState.Error)
        }.stateIn(viewModelScope, shareInPolicy, UIState.Loading)

    private fun bibleReadingItemAsDisplayMode(bibleReading: BibleReadingItem): BibleReadingItemDisplayModel {
        val date = formatDate(bibleReading.date, DateFormat.MonthStringShortAndDateLongInt)
        val readings = bibleReading.readings.toPersistentList()

        return BibleReadingItemDisplayModel(
            id = bibleReading.id,
            date = date,
            readings = readings,
            isMarkedComplete = bibleReading.isMarkedComplete,
            isToday = bibleReading.isToday,
        )
    }

    fun markReadingComplete() {
        viewModelScope.launch {
            val result = markBibleReadingComplete()

            val event = when (result) {
                true -> Event.MarkReadSuccess
                false -> Event.MarkReadFailure
            }

            sendEvent(event)
        }
    }

    fun undoMarkReadingComplete() {
        viewModelScope.launch {
            val result = markBibleReadingIncomplete()

            val event = when (result) {
                true -> Event.UndoMarkReadSuccess
                false -> Event.UndoMarkReadFailure
            }

            sendEvent(event)
        }
    }

    fun requestViewSettings() {
        sendEvent(Event.SettingsRequested)
    }

    fun requestViewStatistics() {
        sendEvent(Event.StatisticsRequested)
    }

    private fun sendEvent(event: Event) {
        viewModelScope.launch { _events.send(event) }
    }

    sealed class UIState {
        data object Loading : UIState()

        data class Content(
            val percentComplete: String,
            val readingItems: PersistentList<BibleReadingItemDisplayModel>
        ) : UIState()

        data object Error : UIState()
    }

    sealed class Event {
        data object MarkReadSuccess : Event()
        data object MarkReadFailure : Event()
        data object UndoMarkReadSuccess : Event()
        data object UndoMarkReadFailure : Event()
        data object SettingsRequested : Event()
        data object StatisticsRequested : Event()
    }
}
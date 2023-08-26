package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingItem
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingComplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingIncomplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToBibleReadings
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToPercentComplete
import com.dladukedev.bibleyeartracker.common.DateFormat
import com.dladukedev.bibleyeartracker.common.FormatDate
import com.dladukedev.bibleyeartracker.common.GenerateID
import com.dladukedev.bibleyeartracker.common.extensions.shareInPolicy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BibleReadingViewModel @Inject constructor(
    subscribeToBibleReadings: SubscribeToBibleReadings,
    subscribeToPercentComplete: SubscribeToPercentComplete,
    private val markBibleReadingComplete: MarkBibleReadingComplete,
    private val markBibleReadingIncomplete: MarkBibleReadingIncomplete,
    private val formatDate: FormatDate,
    private val generateID: GenerateID,
) : ViewModel() {
    private val _events = MutableStateFlow<ImmutableList<Event>>(persistentListOf())
    val events = _events.asStateFlow()

    private val bibleReadingsFlow = subscribeToBibleReadings().map { readings ->
        readings.map { bibleReadingItemAsDisplayMode(it) }.toPersistentList()
    }

    private val percentCompleteFlow = subscribeToPercentComplete().map { percentComplete ->
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

    fun markReadingComplete(id: Int) {
        viewModelScope.launch {
            val result = markBibleReadingComplete(id)

            val event = when (result) {
                true -> Event.MarkReadSuccess(id)
                false -> Event.MarkReadFailure
            }

            sendEvent(event)
        }
    }

    fun undoMarkReadingComplete(id: Int) {
        viewModelScope.launch {
            val result = markBibleReadingIncomplete(id)

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

    private fun sendEvent(event: Event) {
        _events.update { current ->
            event.id = generateID()
            (current + event).toImmutableList()
        }
    }

    fun markEventHandled(event: Event) {
        _events.update { current ->
            current
                .filterNot { it.id == event.id }
                .toImmutableList()
        }

    }

    sealed class UIState {
        object Loading : UIState()

        data class Content(
            val percentComplete: String,
            val readingItems: PersistentList<BibleReadingItemDisplayModel>
        ) : UIState()

        object Error : UIState()
    }

    sealed class Event {
        var id: String? = null

        data class MarkReadSuccess(val readingId: Int) : Event()
        object MarkReadFailure : Event()
        object UndoMarkReadSuccess : Event()
        object UndoMarkReadFailure : Event()
        object SettingsRequested : Event()
    }
}
package com.dladukedev.bibleyeartracker.settings.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dladukedev.bibleyeartracker.bibleReading.domain.ResetReadingProgress
import com.dladukedev.bibleyeartracker.common.DateFormat
import com.dladukedev.bibleyeartracker.common.FormatDate
import com.dladukedev.bibleyeartracker.common.GenerateID
import com.dladukedev.bibleyeartracker.common.extensions.shareInPolicy
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.ObserveSettings
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import com.dladukedev.bibleyeartracker.settings.domain.EditStartDate
import com.dladukedev.bibleyeartracker.settings.domain.EditTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    observeSettings: ObserveSettings,
    private val formatDate: FormatDate,
    private val editStartDate: EditStartDate,
    private val editTheme: EditTheme,
    private val resetReadingProgress: ResetReadingProgress,
    private val generateID: GenerateID,
) : ViewModel() {
    private val _events = MutableStateFlow<ImmutableList<Event>>(persistentListOf())
    val events = _events.asStateFlow()

    val uiState: StateFlow<UIState> = observeSettings()
        .map<AppSettings, UIState> { settings -> settings.asUIState() }
        .catch { emit(UIState.Error) }
        .stateIn(viewModelScope, shareInPolicy, UIState.Loading)

    private fun AppSettings.asUIState(): UIState.Content {
        val startDateFormatted = formatDate(
            date = this.readingGoalStartDate,
            format = DateFormat.MonthLongStringDateShortIntAndYearLongInt
        )

        return UIState.Content(
            startDate = this.readingGoalStartDate,
            startDateFormatted = startDateFormatted,
            theme = this.theme,
        )
    }

    fun updateStartDate(date: LocalDate) {
        viewModelScope.launch {
            editStartDate(date)
        }
    }

    fun updateTheme(theme: Theme) {
        viewModelScope.launch {
            editTheme(theme)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            resetReadingProgress()
            sendEvent(Event.ProgressReset)
        }
    }

    fun requestGoBack() {
        sendEvent(Event.GoBackRequested)
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
        object Error : UIState()
        data class Content(
            val startDate: LocalDate,
            val startDateFormatted: String,
            val theme: Theme,
        ) : UIState()
    }

    sealed class Event {
        var id: String? = null

        object GoBackRequested: Event()
        object ProgressReset: Event()
    }
}
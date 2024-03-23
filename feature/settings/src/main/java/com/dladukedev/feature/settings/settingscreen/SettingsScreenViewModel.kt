package com.dladukedev.feature.settings.settingscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dladukedev.common.models.Theme
import com.dladukedev.common.util.datetime.DateFormat
import com.dladukedev.common.util.datetime.FormatDate
import com.dladukedev.common.util.extensions.shareInPolicy
import com.dladukedev.core.preferences.SetTheme
import com.dladukedev.core.preferences.SubscribeToTheme
import com.dladukedev.core.schedule.ResetProgress
import com.dladukedev.core.schedule.SetStartDate
import com.dladukedev.core.schedule.SubscribeToStartDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val setStartDate: SetStartDate,
    private val setTheme: SetTheme,
    private val resetProgress: ResetProgress,
    private val formatDate: FormatDate,
    subscribeToStartDate: SubscribeToStartDate,
    subscribeToTheme: SubscribeToTheme,
) : ViewModel() {
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    val uiState: StateFlow<UIState> = combine(
        subscribeToStartDate.values,
        subscribeToTheme.values,
    ) { startDate, theme ->
        val startDateFormatted = formatDate(
            date = startDate,
            format = DateFormat.MonthLongStringDateShortIntAndYearLongInt
        )

        UIState.Content(startDate, startDateFormatted, theme)
    }.catch<UIState> { emit(UIState.Error) }
        .stateIn(viewModelScope, shareInPolicy, UIState.Loading)

    fun updateStartDate(date: LocalDate) {
        viewModelScope.launch {
            setStartDate(date)
        }
    }

    fun updateTheme(theme: com.dladukedev.common.models.Theme) {
        viewModelScope.launch {
            setTheme(theme)
        }
    }

    fun resetProgress() {
        viewModelScope.launch {
            resetProgress.invoke()
            sendEvent(Event.ProgressReset)
        }
    }

    fun requestGoBack() {
        sendEvent(Event.GoBackRequested)
    }

    private fun sendEvent(event: Event) {
        viewModelScope.launch {
           _events.send(event)
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
        data object GoBackRequested: Event()
        data object ProgressReset: Event()
    }
}
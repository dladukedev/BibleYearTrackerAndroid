@file:OptIn(ExperimentalMaterial3Api::class)

package com.dladukedev.bibleyeartracker.settings.display

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.common.extensions.asLocalDate
import com.dladukedev.bibleyeartracker.common.extensions.toMilliseconds
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import java.time.LocalDate

@Stable
data class DialogState(
    val isShown: Boolean,
    val showModal: () -> Unit,
    val confirmModal: () -> Unit,
    val dismissModal: () -> Unit,
)

@Stable
data class ThemePickerState(
    val selectedTheme: Theme,
    val onThemeSelected: (Theme) -> Unit,
)

@Stable
sealed class SettingScreenState(val requestGoBack: () -> Unit, val snackbarHostState: SnackbarHostState) {
    data class Loading(
        private val _requestGoBack: () -> Unit,
        private val _snackbarHostState: SnackbarHostState,
        ) : SettingScreenState(_requestGoBack, _snackbarHostState)
    data class Content(
        val uiState: SettingsScreenViewModel.UIState.Content,
        val datePickerState: DatePickerState,
        val startDatePickerModalState: DialogState,
        val themePickerDialogState: DialogState,
        val themePickerState: ThemePickerState,
        val resetProgressDialogState: DialogState,
        private val _requestGoBack: () -> Unit,
        private val _snackbarHostState: SnackbarHostState,
    ) : SettingScreenState(_requestGoBack, _snackbarHostState)

    data class Error(
        private val _requestGoBack: () -> Unit,
        private val _snackbarHostState: SnackbarHostState,
        ) : SettingScreenState(_requestGoBack, _snackbarHostState)
}

@Composable
fun rememberSettingScreenStateHolder(
    state: SettingsScreenViewModel.UIState,
    events: ImmutableList<SettingsScreenViewModel.Event>,
    goBack: () -> Unit,
    requestGoBack: () -> Unit,
    markEventHandled: (SettingsScreenViewModel.Event) -> Unit,
    updateStartDate: (LocalDate) -> Unit,
    updateTheme: (Theme) -> Unit,
    resetProgress: () -> Unit,
    datePickerState: DatePickerState = rememberDatePickerState(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): SettingScreenState {
    val coroutineScope = rememberCoroutineScope()
    val resetProgressSnackbarMessage =
        stringResource(id = R.string.settings_reset_progress_success_snackbar)

    LaunchedEffect(events) {
        events.firstOrNull()?.let { current ->
            when (current) {
                SettingsScreenViewModel.Event.GoBackRequested -> {
                    goBack()
                }

                SettingsScreenViewModel.Event.ProgressReset -> {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(resetProgressSnackbarMessage)
                    }
                }
            }

            markEventHandled(current)
        }
    }

    return when (state) {
        SettingsScreenViewModel.UIState.Error -> {
            remember(requestGoBack) {
                SettingScreenState.Error(requestGoBack, snackbarHostState)
            }
        }

        SettingsScreenViewModel.UIState.Loading -> {
            remember(goBack) {
                SettingScreenState.Loading(requestGoBack, snackbarHostState)
            }
        }

        is SettingsScreenViewModel.UIState.Content -> {
            var isStartDateDialogShown by rememberSaveable { mutableStateOf(false) }
            var isThemeDialogShown by rememberSaveable { mutableStateOf(false) }
            var isResetProgressDialogShown by rememberSaveable { mutableStateOf(false) }
            var selectedTheme by rememberSaveable { mutableStateOf(state.theme) }

            val startDatePickerModalState = remember(isStartDateDialogShown, state.startDate) {
                DialogState(
                    isShown = isStartDateDialogShown,
                    showModal = {
                        datePickerState.selectedDateMillis = state.startDate.toMilliseconds()
                        isStartDateDialogShown = true
                    },
                    confirmModal = {
                        isStartDateDialogShown = false
                        val selectedDateInMillis = datePickerState.selectedDateMillis
                        if (selectedDateInMillis != null) {
                            val date = selectedDateInMillis.asLocalDate()
                            updateStartDate(date)
                        }
                    },
                    dismissModal = {
                        isStartDateDialogShown = false
                    },
                )
            }

            val themePickerDialogState = remember(isThemeDialogShown) {
                DialogState(
                    isShown = isThemeDialogShown,
                    showModal = {
                        selectedTheme = state.theme
                        isThemeDialogShown = true
                    },
                    confirmModal = {
                        updateTheme(selectedTheme)
                        isThemeDialogShown = false
                    },
                    dismissModal = {
                        isThemeDialogShown = false
                    },
                )
            }

            val themePickerState = remember(selectedTheme) {
                ThemePickerState(
                    selectedTheme = selectedTheme,
                    onThemeSelected = { selectedTheme = it })
            }

            val resetProgressDialogState = remember(isResetProgressDialogShown) {
                DialogState(
                    isShown = isResetProgressDialogShown,
                    showModal = {
                        isResetProgressDialogShown = true
                    },
                    confirmModal = {
                        resetProgress()
                        isResetProgressDialogShown = false
                    },
                    dismissModal = {
                        isResetProgressDialogShown = false
                    },
                )
            }

            remember(
                state,
                datePickerState,
                startDatePickerModalState,
                themePickerDialogState,
                themePickerState,
                resetProgressDialogState,
                requestGoBack,
                snackbarHostState,
            ) {
                SettingScreenState.Content(
                    uiState = state,
                    datePickerState = datePickerState,
                    startDatePickerModalState = startDatePickerModalState,
                    themePickerDialogState = themePickerDialogState,
                    themePickerState = themePickerState,
                    resetProgressDialogState = resetProgressDialogState,
                    _snackbarHostState = snackbarHostState,
                    _requestGoBack = requestGoBack,
                )
            }
        }
    }
}

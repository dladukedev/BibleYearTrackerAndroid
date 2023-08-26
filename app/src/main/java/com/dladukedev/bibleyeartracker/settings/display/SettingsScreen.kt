@file:OptIn(ExperimentalMaterial3Api::class)

package com.dladukedev.bibleyeartracker.settings.display

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.app.theme.Spacing
import com.dladukedev.bibleyeartracker.common.display.ErrorComponent
import com.dladukedev.bibleyeartracker.common.display.LoadingComponent
import com.dladukedev.bibleyeartracker.settings.domain.Theme


@Composable
fun SettingsScreen(
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val vmState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState()

    val state = rememberSettingScreenStateHolder(
        state = vmState,
        events = events,
        markEventHandled = viewModel::markEventHandled,
        goBack = goBack,
        requestGoBack = viewModel::requestGoBack,
        updateTheme = viewModel::updateTheme,
        updateStartDate = viewModel::updateStartDate,
        resetProgress = viewModel::resetProgress,
    )

    SettingsScreen(state = state, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingScreenState,
    modifier: Modifier = Modifier,
) {
    val goBackContentDescription = stringResource(id = R.string.settings_go_back_content_description)


    Scaffold(
        snackbarHost = { SnackbarHost(state.snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = state.requestGoBack, modifier = Modifier.semantics { contentDescription = goBackContentDescription }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                })
        },
        modifier = modifier,
    ) { paddingValues ->
        when (state) {
            is SettingScreenState.Content -> {
                SettingsScreenContent(
                    state = state.uiState,
                    startDatePickerModalState = state.startDatePickerModalState,
                    themePickerDialogState = state.themePickerDialogState,
                    resetProgressDialogState = state.resetProgressDialogState,
                    datePickerState = state.datePickerState,
                    themePickerState = state.themePickerState,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            is SettingScreenState.Error -> ErrorComponent()
            is SettingScreenState.Loading -> LoadingComponent()
        }
    }

}

@Composable
private fun SettingsScreenContent(
    state: SettingsScreenViewModel.UIState.Content,
    startDatePickerModalState: DialogState,
    themePickerDialogState: DialogState,
    resetProgressDialogState: DialogState,
    datePickerState: DatePickerState,
    themePickerState: ThemePickerState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        SettingsSection(label = stringResource(id = R.string.settings_goals_section_title)) {
            SettingsItem(
                description = stringResource(id = R.string.settings_start_date_item_description),
                currentValue = state.startDateFormatted,
                onClick = startDatePickerModalState.showModal,
            )
        }
        Divider()
        SettingsSection(label = stringResource(id = R.string.settings_appearance_section_title)) {
            SettingsItem(
                description = stringResource(id = R.string.settings_theme_item_description),
                currentValue = when (state.theme) {
                    Theme.SYSTEM -> stringResource(id = R.string.settings_theme_system_item)
                    Theme.DARK -> stringResource(id = R.string.settings_theme_dark_item)
                    Theme.LIGHT -> stringResource(id = R.string.settings_theme_light_item)
                },
                onClick = themePickerDialogState.showModal,
            )
        }
        Divider()
        SettingsSection(label = stringResource(id = R.string.settings_advanced_section_title)) {
            Text(
                text = stringResource(id = R.string.settings_reset_progress_item_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .clickable(onClick = resetProgressDialogState.showModal)
                    .fillMaxWidth()
                    .padding(
                        vertical = Spacing.MEDIUM,
                        horizontal = Spacing.LARGE,
                    )
            )
        }
    }
    StartDatePickerDialog(
        dialogState = startDatePickerModalState,
        datePickerState = datePickerState
    )
    ThemePickerDialog(
        dialogState = themePickerDialogState,
        themePickerState = themePickerState
    )
    ResetProgressDialog(dialogState = resetProgressDialogState)
}

@Composable
private fun SettingsSection(
    label: String, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                vertical = Spacing.MEDIUM,
                horizontal = Spacing.LARGE,
            )
        )
        content()
    }
}

@Composable
private fun SettingsItem(
    description: String,
    currentValue: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .clickable { onClick() }
        .fillMaxWidth()
        .padding(
            vertical = Spacing.MEDIUM,
            horizontal = Spacing.LARGE,
        )) {
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = currentValue,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
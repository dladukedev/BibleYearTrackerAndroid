package com.dladukedev.feature.settings.settingscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.dladukedev.common.ui.values.Spacing
import com.dladukedev.feature.settings.R
import com.dladukedev.common.models.Theme
import com.dladukedev.feature.settings.settingscreen.DialogState
import com.dladukedev.feature.settings.settingscreen.ThemePickerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePickerDialog(
    dialogState: DialogState,
    themePickerState: ThemePickerState,
    modifier: Modifier = Modifier,
) {
    if (dialogState.isShown) {
        BasicAlertDialog(
            onDismissRequest = dialogState.dismissModal,
            modifier = modifier
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = Spacing.MEDIUM
                        )
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(Spacing.SMALL)
                ) {
                    Text(
                        text = stringResource(id = R.string.settings_theme_dialog_title),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = Spacing.MEDIUM)
                    )
                    Column(modifier = Modifier.selectableGroup()) {
                        ThemeDialogItem(
                            label = stringResource(id = R.string.settings_theme_light_item),
                            theme = com.dladukedev.common.models.Theme.LIGHT,
                            themePickerState = themePickerState,
                        )
                        ThemeDialogItem(
                            label = stringResource(id = R.string.settings_theme_dark_item),
                            theme = com.dladukedev.common.models.Theme.DARK,
                            themePickerState = themePickerState,
                        )
                        ThemeDialogItem(
                            label = stringResource(id = R.string.settings_theme_system_item),
                            theme = com.dladukedev.common.models.Theme.SYSTEM,
                            themePickerState = themePickerState,
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Spacing.LARGE),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = dialogState.dismissModal) {
                            Text(text = stringResource(id = R.string.generic_cancel))
                        }
                        TextButton(onClick = dialogState.confirmModal) {
                            Text(text = stringResource(id = R.string.generic_update))
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ThemeDialogItem(
    label: String,
    theme: com.dladukedev.common.models.Theme,
    themePickerState: ThemePickerState,
    modifier: Modifier = Modifier,
) {
    val selected = theme == themePickerState.selectedTheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = { themePickerState.onThemeSelected(theme) },
                role = Role.RadioButton
            )
            .padding(horizontal = Spacing.MEDIUM, vertical = Spacing.SMALL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected, onClick = null
        )
        Spacer(modifier = Modifier.width(Spacing.LARGE))
        Text(
            text = label, style = MaterialTheme.typography.bodyMedium
        )
    }
}



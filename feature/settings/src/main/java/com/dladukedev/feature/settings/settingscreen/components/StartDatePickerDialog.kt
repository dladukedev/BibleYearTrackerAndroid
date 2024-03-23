package com.dladukedev.feature.settings.settingscreen.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dladukedev.feature.settings.R
import com.dladukedev.feature.settings.settingscreen.DialogState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePickerDialog(
    dialogState: DialogState,
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
) {
    if (dialogState.isShown) {
        DatePickerDialog(
            onDismissRequest = dialogState.dismissModal,
            confirmButton = {
                TextButton(
                    onClick = dialogState.confirmModal,
                ) {
                    Text(stringResource(id = R.string.generic_update))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = dialogState.dismissModal,
                ) {
                    Text(stringResource(id = R.string.generic_cancel))
                }
            },
            modifier = modifier,
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
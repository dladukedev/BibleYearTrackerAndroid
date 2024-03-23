package com.dladukedev.feature.settings.settingscreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dladukedev.feature.settings.R
import com.dladukedev.feature.settings.settingscreen.DialogState

@Composable
fun ResetProgressDialog(
    dialogState: DialogState,
    modifier: Modifier = Modifier,
) {
    if (dialogState.isShown) {
        AlertDialog(
            onDismissRequest = dialogState.dismissModal,
            confirmButton = {
                TextButton(onClick = dialogState.confirmModal) {
                    Text(
                        text = stringResource(id = R.string.settings_reset_progress_confirm),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = dialogState.dismissModal) {
                    Text(text = stringResource(id = R.string.generic_cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.settings_reset_progress_dialog_title),
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.settings_reset_progress_dialog_description),
                )
            },
            modifier = modifier,
        )
    }
}
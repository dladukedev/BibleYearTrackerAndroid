package com.dladukedev.feature.statistics.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.dladukedev.feature.statistics.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsHeader(state: StatisticsHeaderState) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = state.onBackPressed,
                modifier = Modifier.semantics {
                    contentDescription = state.onBackPressedContentDescription
                }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        title = { Text(text = state.title) },
    )
}

data class StatisticsHeaderState(
    val title: String,
    val onBackPressed: () -> Unit,
    val onBackPressedContentDescription: String,
)

@Composable
fun rememberStatisticsHeaderState(
    onBackPressed: () -> Unit,
): StatisticsHeaderState {
    val title = stringResource(id = R.string.statistics_title)
    val onBackPressedContentDescription = stringResource(id = R.string.generic_go_back)

    return remember(onBackPressed, title, onBackPressedContentDescription) {
        StatisticsHeaderState(
            title = title,
            onBackPressed = onBackPressed,
            onBackPressedContentDescription = onBackPressedContentDescription,
        )
    }
}

val previewStatisticsHeaderState = StatisticsHeaderState(
    title = "Statistics",
    onBackPressed = {},
    onBackPressedContentDescription = "",
)

@Preview
@Composable
private fun StatisticsHeaderPreview() {
    StatisticsHeader(state = previewStatisticsHeaderState)
}
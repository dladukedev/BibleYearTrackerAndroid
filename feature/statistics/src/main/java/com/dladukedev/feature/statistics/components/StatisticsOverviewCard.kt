package com.dladukedev.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dladukedev.common.ui.values.Spacing
import com.dladukedev.core.statistics.Stats
import com.dladukedev.feature.statistics.R
import kotlin.math.abs

data class StatisticsOverviewCardState(
    val title: String,
    val progressPercent: String,
    val progressDaysComplete: String,
    val progressDaysRemaining: String,
    val daysOffTarget: String,
)

@Composable
internal fun StatisticsOverviewCard(
    state: StatisticsOverviewCardState,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(all = Spacing.MEDIUM),
            verticalArrangement = Arrangement.spacedBy(Spacing.X_SMALL)
        ) {
            Text(text = state.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = state.progressPercent, modifier = Modifier.padding(start = Spacing.MEDIUM))
            Text(
                text = state.progressDaysComplete,
                modifier = Modifier.padding(start = Spacing.MEDIUM)
            )
            Text(
                text = state.progressDaysRemaining,
                modifier = Modifier.padding(start = Spacing.MEDIUM)
            )
            Text(text = state.daysOffTarget, modifier = Modifier.padding(start = Spacing.MEDIUM))
        }
    }
}

@Composable
internal fun rememberStatisticsOverviewCardState(
    stats: Stats,
): StatisticsOverviewCardState {
    val title = stringResource(id = R.string.statistics_overview_card_title)
    val progressPercentComplete = stringResource(
        id = R.string.statistics_overview_card_percentComplete,
        stats.percentComplete
    )
    val progressDaysComplete = stringResource(
        id = R.string.statistics_overview_card_daysComplete,
        stats.daysComplete,
        stats.daysTotal
    )
    val progressDaysRemaining =
        stringResource(id = R.string.statistics_overview_card_daysRemaining, stats.daysRemaining)

    val progressDaysOffTarget = when {
        stats.daysOffTarget > 0 -> stringResource(
            id = R.string.statistics_overview_card_daysOffTarget_ahead,
            abs(stats.daysOffTarget)
        )

        stats.daysOffTarget < 0 -> stringResource(
            id = R.string.statistics_overview_card_daysOffTarget_behind,
            abs(stats.daysOffTarget)
        )

        else -> stringResource(id = R.string.statistics_overview_card_daysOffTarget_onTarget)
    }

    return StatisticsOverviewCardState(
        title = title,
        progressPercent = progressPercentComplete,
        progressDaysComplete = progressDaysComplete,
        progressDaysRemaining = progressDaysRemaining,
        daysOffTarget = progressDaysOffTarget,
    )
}

internal val previewStatisticsOverviewCardState = StatisticsOverviewCardState(
    title = "Overview",
    progressPercent = "10.00% Complete",
    progressDaysComplete = "36/365 Days Complete",
    progressDaysRemaining = "329 Day(s) Remaining",
    daysOffTarget = "10 Day(s) Behind",
)

@Preview
@Composable
private fun StatisticsOverviewCardPreview() {
    StatisticsOverviewCard(state = previewStatisticsOverviewCardState)
}
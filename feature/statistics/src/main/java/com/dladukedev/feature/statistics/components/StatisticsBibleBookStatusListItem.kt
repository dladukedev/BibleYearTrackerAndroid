package com.dladukedev.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.HorizontalRule
import androidx.compose.material.icons.outlined.IndeterminateCheckBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dladukedev.common.ui.BibleBookNameLookup
import com.dladukedev.common.ui.values.Spacing
import com.dladukedev.core.statistics.BibleBookReadState
import com.dladukedev.core.statistics.ReadStatus
import com.dladukedev.feature.statistics.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList


@Composable
fun StatisticsBibleBookReadStatusGroupTitle(title: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.MEDIUM),
        modifier = modifier
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        HorizontalDivider()
    }
}

@Composable
fun StatisticsBibleBookReadStatusListItem(
    state: StatisticsBibleBookReadStatusListItemState,
    modifier: Modifier = Modifier
) {
    val icon = when (state.readStatus) {
        ReadStatus.UNREAD -> null
        ReadStatus.READ -> Icons.Outlined.Check
        ReadStatus.IN_PROGRESS -> Icons.Outlined.HorizontalRule
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.SMALL),
        modifier = modifier
            .padding(horizontal = Spacing.SMALL, vertical = Spacing.X_SMALL)
            .clearAndSetSemantics {
                contentDescription = "${state.bookName} ${state.readStatusContentDescription}"
            }
    ) {
        Box(modifier = Modifier.size(32.dp)) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Text(text = state.bookName, style = MaterialTheme.typography.titleLarge)
    }
}

class StatisticsBibleBookReadStatusListItemState(
    val bookName: String,
    val readStatus: ReadStatus,
    val readStatusContentDescription: String,
)

class StatisticsBibleBookReadStatusListState(
    val oldTestamentTitle: String,
    val oldTestamentItems: ImmutableList<StatisticsBibleBookReadStatusListItemState>,
    val newTestamentTitle: String,
    val newTestamentItems: ImmutableList<StatisticsBibleBookReadStatusListItemState>,
)

@Composable
fun rememberStatisticsBibleBookReadStatusListState(
    bookStatuses: ImmutableList<BibleBookReadState>
): StatisticsBibleBookReadStatusListState {
    val readStatusContentDescriptionLookup = mapOf(
        ReadStatus.READ to stringResource(id = R.string.statistics_bibleReadStatus_item_status_read),
        ReadStatus.UNREAD to stringResource(id = R.string.statistics_bibleReadStatus_item_status_unread),
        ReadStatus.IN_PROGRESS to stringResource(id = R.string.statistics_bibleReadStatus_item_status_inProgress),
    )

    val (oldTestamentItems, newTestamentItems) = bookStatuses.map { bookReadState ->
        val itemState = StatisticsBibleBookReadStatusListItemState(
            bookName = stringResource(id = BibleBookNameLookup.getBibleNameString(bookReadState.book)),
            readStatus = bookReadState.status,
            readStatusContentDescription = readStatusContentDescriptionLookup.getValue(bookReadState.status)
        )
        Pair(itemState, bookReadState.book.isOldTestament)
    }.partition { it.second }

    val oldTestamentCount = oldTestamentItems.size
    val oldTestamentReadCount = oldTestamentItems.count { it.first.readStatus == ReadStatus.READ }
    val oldTestamentTitle = stringResource(
        id = R.string.statistics_bibleReadStatus_header_oldTestament,
        oldTestamentReadCount,
        oldTestamentCount,
    )

    val newTestamentCount = newTestamentItems.size
    val newTestamentReadCount = newTestamentItems.count { it.first.readStatus == ReadStatus.READ }
    val newTestamentTitle = stringResource(
        id = R.string.statistics_bibleReadStatus_header_newTestament,
        newTestamentReadCount,
        newTestamentCount
    )

    return remember(oldTestamentTitle, oldTestamentItems, newTestamentTitle, newTestamentItems) {
        StatisticsBibleBookReadStatusListState(
            oldTestamentTitle = oldTestamentTitle,
            oldTestamentItems = oldTestamentItems.map { it.first }.toImmutableList(),
            newTestamentTitle = newTestamentTitle,
            newTestamentItems = newTestamentItems.map { it.first }.toImmutableList(),
        )
    }
}

val previewStatisticsBibleBookReadStatusListState = StatisticsBibleBookReadStatusListState(
    oldTestamentTitle = "Old Testament (2/5)",
    oldTestamentItems = persistentListOf(
        StatisticsBibleBookReadStatusListItemState("Genesis", ReadStatus.READ, ""),
        StatisticsBibleBookReadStatusListItemState("Exodus", ReadStatus.READ, ""),
        StatisticsBibleBookReadStatusListItemState("Leviticus", ReadStatus.IN_PROGRESS, ""),
        StatisticsBibleBookReadStatusListItemState("Numbers", ReadStatus.UNREAD, ""),
        StatisticsBibleBookReadStatusListItemState("Deuteronomy", ReadStatus.UNREAD, ""),
    ),
    newTestamentTitle = "New Testament (1/4)",
    newTestamentItems = persistentListOf(
        StatisticsBibleBookReadStatusListItemState("Matthew", ReadStatus.READ, ""),
        StatisticsBibleBookReadStatusListItemState("Mark", ReadStatus.IN_PROGRESS, ""),
        StatisticsBibleBookReadStatusListItemState("Luke", ReadStatus.UNREAD, ""),
        StatisticsBibleBookReadStatusListItemState("John", ReadStatus.UNREAD, ""),
    ),
)

@Preview
@Composable
private fun StatisticsBibleBookReadStatusListPreview() {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            StatisticsBibleBookReadStatusGroupTitle(title = previewStatisticsBibleBookReadStatusListState.oldTestamentTitle)
        }
        items(
            previewStatisticsBibleBookReadStatusListState.oldTestamentItems,
            key = { it.bookName }) { bookStatusItemState ->
            StatisticsBibleBookReadStatusListItem(state = bookStatusItemState)
        }
        item {
            StatisticsBibleBookReadStatusGroupTitle(title = previewStatisticsBibleBookReadStatusListState.newTestamentTitle)
        }
        items(
            previewStatisticsBibleBookReadStatusListState.newTestamentItems,
            key = { it.bookName }) { bookStatusItemState ->
            StatisticsBibleBookReadStatusListItem(state = bookStatusItemState)
        }
    }
}
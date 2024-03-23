package com.dladukedev.feature.readings.readinglistscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.dladukedev.common.models.BibleBook
import com.dladukedev.common.models.Reading
import com.dladukedev.feature.readings.readinglistscreen.BibleReadingItemDisplayModel
import com.dladukedev.common.ui.values.Spacing
import com.dladukedev.feature.readings.R
import kotlinx.collections.immutable.persistentListOf

@Composable
fun BibleReadingListItem(
    readingItem: BibleReadingItemDisplayModel,
    modifier: Modifier = Modifier
) {
    val itemContentDescription = bibleReadingContentDescription(readingItem = readingItem)

    Row(
        modifier = modifier
            .padding(
                horizontal = Spacing.LARGE,
                vertical = Spacing.SMALL,
            )
            .clearAndSetSemantics { contentDescription = itemContentDescription },
        horizontalArrangement = Arrangement.spacedBy(Spacing.MEDIUM),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (readingItem.isMarkedComplete) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "")
        }
        Column {
            BibleReadingDateDisplay(date = readingItem.date, isToday = readingItem.isToday)
            if (readingItem.isToday) {
                Text(
                    text = stringResource(id = R.string.generic_today),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            readingItem.readings.forEach { reading ->
                BibleReadingText(reading = reading)
            }
        }
    }
}

@Composable
fun BibleReadingDateDisplay(date: String, isToday: Boolean) {
    val style = MaterialTheme.typography.titleLarge.let {
        when {
            isToday -> it.copy(fontWeight = FontWeight.ExtraBold)
            else -> it
        }
    }

    Text(text = date, style = style)
}

@Composable
private fun bibleReadingContentDescription(readingItem: BibleReadingItemDisplayModel): String {
    val todayContentDescription = stringResource(id = R.string.generic_today)
    val completeContentDescription =
        stringResource(id = R.string.reading_complete_content_description)

    val selections = readingItem.readings.map { reading ->
        readingContentDescription(reading)
    }.joinToString(", ")

    return """
        ${readingItem.date}
        ${if (readingItem.isToday) todayContentDescription else ""}
        ${if (readingItem.isMarkedComplete) completeContentDescription else ""}
        $selections
    """.trimIndent()
}

@Preview
@Composable
fun PreviewBibleReadingListItem() {
    val item = BibleReadingItemDisplayModel(
        id = 1,
        date = "Jan 1",
        readings = persistentListOf(
            Reading.PartialChapters(
                book = BibleBook.FIRST_JOHN,
                chapterStart = "1",
                verseStart = "1",
                chapterEnd = "3",
                verseEnd = "10"
            ),
            Reading.SingleChapter(
                book = BibleBook.PSALMS,
                chapter = "1",
            ),
            Reading.PartialChapter(
                book = BibleBook.MATTHEW,
                chapter = "1",
                verseStart = "1",
                verseEnd = "17",
            ),
        ),
        isMarkedComplete = false,
        isToday = true,
    )

    MaterialTheme {
        BibleReadingListItem(readingItem = item)
    }
}
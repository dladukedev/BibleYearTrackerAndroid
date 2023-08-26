package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.app.theme.Spacing
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleBook
import com.dladukedev.bibleyeartracker.bibleReading.domain.Reading
import kotlinx.collections.immutable.persistentListOf

@Composable
fun NextBibleReading(
    bibleReading: BibleReadingItemDisplayModel,
    onCompleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.MEDIUM)
        ) {
            NextBibleReadingTitle(bibleReading = bibleReading)
            Spacer(modifier = Modifier.height(Spacing.SMALL))
            bibleReading.readings.forEach { reading ->
                val bibleReadingContentDescription = readingContentDescription(
                    reading = reading
                )
                BibleReadingText(
                    reading = reading,
                    modifier = Modifier
                        .padding(start = Spacing.MEDIUM)
                        .semantics {
                            contentDescription = bibleReadingContentDescription
                        }
                )
            }
            FilledTonalButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { onCompleteClick(bibleReading.id) }) {
                Text(text = stringResource(id = R.string.reading_list_complete_action))
            }
        }
    }
}

@Composable
private fun NextBibleReadingTitle(
    bibleReading: BibleReadingItemDisplayModel,
    modifier: Modifier = Modifier
) {
    val today = stringResource(id = R.string.generic_today)

    val text = if (bibleReading.isToday) {
        "${bibleReading.date} ($today)"
    } else {
        bibleReading.date
    }

    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
    )
}

@Composable
@Preview
fun NextBibleReadingItemPreview() {
    val bibleReading = BibleReadingItemDisplayModel(
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
        isToday = false,
    )

    NextBibleReading(bibleReading = bibleReading, {})
}
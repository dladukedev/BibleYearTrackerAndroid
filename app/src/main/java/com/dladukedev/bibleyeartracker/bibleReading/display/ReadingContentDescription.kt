package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.bibleReading.domain.Reading

@Composable
fun readingContentDescription(reading: Reading): String {
    val bookContentDescription =
        stringResource(id = BibleBookNameLookup.getBibleNameString(reading.book))

    return when (reading) {
        is Reading.MultipleChapters -> {
            stringResource(
                id = R.string.reading_multiple_chapters_content_description,
                bookContentDescription,
                reading.chapterStart,
                reading.chapterEnd
            )
        }

        is Reading.PartialChapter -> {
            stringResource(
                id = R.string.reading_partial_chapter_content_description,
                bookContentDescription,
                reading.chapter,
                reading.verseStart,
                reading.verseEnd
            )
        }

        is Reading.PartialChapters -> {
            stringResource(
                id = R.string.reading_partial_chapters_content_description,
                bookContentDescription,
                reading.chapterStart,
                reading.verseStart,
                reading.chapterEnd,
                reading.verseEnd
            )
        }

        is Reading.SingleChapter -> {
            stringResource(
                id = R.string.reading_single_chapter_content_description,
                bookContentDescription,
                reading.chapter,
            )
        }

        is Reading.WholeBook -> {
            stringResource(
                id = R.string.reading_whole_book_content_description,
                bookContentDescription
            )
        }
    }
}

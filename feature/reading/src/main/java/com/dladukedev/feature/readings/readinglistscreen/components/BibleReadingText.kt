package com.dladukedev.feature.readings.readinglistscreen.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dladukedev.common.ui.BibleBookNameLookup
import com.dladukedev.common.models.Reading

@Composable
fun BibleReadingText(reading: Reading, modifier: Modifier = Modifier) {
    val display = rememberReadingDisplay(reading = reading)

    Text(text = display, style = MaterialTheme.typography.bodyMedium, modifier = modifier)
}


@Composable
private fun rememberReadingDisplay(reading: Reading): String {
    val bookDisplay =
        stringResource(id = BibleBookNameLookup.getBibleNameString(reading.book))

    return when (reading) {
        is Reading.MultipleChapters -> {
            "$bookDisplay ${reading.chapterStart}-${reading.chapterEnd}"
        }

        is Reading.PartialChapter -> {
            "$bookDisplay ${reading.chapter}:${reading.verseStart}-${reading.verseEnd}"
        }

        is Reading.PartialChapters -> {
            "$bookDisplay ${reading.chapterStart}:${reading.verseStart}-${reading.chapterEnd}:${reading.verseEnd}"
        }

        is Reading.SingleChapter -> {
            "$bookDisplay ${reading.chapter}"
        }

        is Reading.WholeBook -> {
            bookDisplay
        }
    }
}

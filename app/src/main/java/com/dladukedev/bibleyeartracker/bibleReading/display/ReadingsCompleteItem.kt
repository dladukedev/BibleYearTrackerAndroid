package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.dladukedev.bibleyeartracker.R
import com.dladukedev.bibleyeartracker.app.theme.BibleYearTrackerTheme
import com.dladukedev.bibleyeartracker.app.theme.Spacing

@Composable
fun ReadingsCompleteItem(modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.MEDIUM)
                .semantics(mergeDescendants = true) { }
        ) {
            Text(
                text = stringResource(id = R.string.reading_list_complete_title),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(Spacing.SMALL))
            Text(text = stringResource(id = R.string.reading_list_complete_subtitle))
        }
    }
}

@Preview
@Composable
fun ReadingsCompleteItemPreview() {
    BibleYearTrackerTheme {
        ReadingsCompleteItem()
    }
}
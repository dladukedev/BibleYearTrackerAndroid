package com.dladukedev.bibleyeartracker.bibleReading.display

import androidx.compose.runtime.Immutable
import com.dladukedev.bibleyeartracker.bibleReading.domain.Reading
import kotlinx.collections.immutable.PersistentList

@Immutable
data class BibleReadingItemDisplayModel(
    val id: Int,
    val date: String,
    val readings: PersistentList<Reading>,
    val isMarkedComplete: Boolean,
    val isToday: Boolean,
)
package com.dladukedev.feature.readings.readinglistscreen

import androidx.compose.runtime.Immutable
import com.dladukedev.common.models.Reading
import kotlinx.collections.immutable.PersistentList

@Immutable
data class BibleReadingItemDisplayModel(
    val id: Int,
    val date: String,
    val readings: PersistentList<Reading>,
    val isMarkedComplete: Boolean,
    val isToday: Boolean,
)
package com.dladukedev.bibleyeartracker.bibleReading.domain

import java.time.LocalDate

data class BibleReadingItem(
    val id: Int,
    val date: LocalDate,
    val readings: List<Reading>,
    val isMarkedComplete: Boolean,
    val isToday: Boolean,
)
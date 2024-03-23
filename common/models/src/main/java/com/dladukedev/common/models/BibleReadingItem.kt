package com.dladukedev.common.models

import java.time.LocalDate

data class BibleReadingItem(
    val id: Int,
    val date: LocalDate,
    val readings: List<Reading>,
    val isMarkedComplete: Boolean,
    val isToday: Boolean,
)
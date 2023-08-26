package com.dladukedev.bibleyeartracker.bibleReading.data

import kotlinx.serialization.Serializable

@Serializable
data class BibleReadingRoot(
    val days: List<BibleReadingDataModel>,
)

@Serializable
data class BibleReadingDataModel(
    val id: Int,
    val offset: Int,
    val reading1: String,
    val reading2: String,
    val reading3: String,
)
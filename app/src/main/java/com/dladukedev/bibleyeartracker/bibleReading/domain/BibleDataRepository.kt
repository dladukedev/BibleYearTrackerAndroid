package com.dladukedev.bibleyeartracker.bibleReading.domain

interface BibleDataRepository {
    suspend fun getBibleReadings(): List<BibleReadingDay>
    suspend fun getReading(readingId: Int): BibleReadingDay?
}
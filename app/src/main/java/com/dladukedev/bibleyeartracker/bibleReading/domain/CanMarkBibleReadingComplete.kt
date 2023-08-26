package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface CanMarkBibleReadingComplete {
    suspend operator fun invoke(reading: BibleReadingDay): Boolean
}

class CanMarkBibleReadingCompleteUseCase @Inject constructor(
    private val readInformationRepository: ReadInformationRepository,
) : CanMarkBibleReadingComplete {
    override suspend fun invoke(reading: BibleReadingDay): Boolean {
        val currentLastReadOffset = readInformationRepository
            .getLastReadOffset()

        val isNextDay = (currentLastReadOffset ?: -1) + 1 == reading.offset

        return isNextDay
    }

}
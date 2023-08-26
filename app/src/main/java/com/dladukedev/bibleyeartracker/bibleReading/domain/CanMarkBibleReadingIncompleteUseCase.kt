package com.dladukedev.bibleyeartracker.bibleReading.domain

import javax.inject.Inject

interface CanMarkBibleReadingIncomplete {
    suspend operator fun invoke(reading: BibleReadingDay): Boolean
}

class CanMarkBibleReadingIncompleteUseCase @Inject constructor(
    private val readInformationRepository: ReadInformationRepository,
) : CanMarkBibleReadingIncomplete {
    override suspend fun invoke(reading: BibleReadingDay): Boolean {
        val currentLastReadOffset = readInformationRepository
            .getLastReadOffset()

        if(currentLastReadOffset == null) return false

        val isPreviousDay = currentLastReadOffset == reading.offset

        return isPreviousDay
    }

}

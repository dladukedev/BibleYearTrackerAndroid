package com.dladukedev.bibleyeartracker.bibleReading.domain

import javax.inject.Inject

interface MarkBibleReadingIncomplete {
    suspend operator fun invoke(bibleReadingId: Int): Boolean
}

class MarkBibleReadingIncompleteUseCase @Inject constructor(
    private val bibleDataRepository: BibleDataRepository,
    private val readInformationRepository: ReadInformationRepository,
    private val canMarkBibleReadingIncomplete: CanMarkBibleReadingIncomplete,
) : MarkBibleReadingIncomplete {
    override suspend fun invoke(bibleReadingId: Int): Boolean {
        val reading = bibleDataRepository.getReading(bibleReadingId)

        if (reading == null) return false

        val isValidToMarkIncomplete = canMarkBibleReadingIncomplete(reading)

        if (isValidToMarkIncomplete.not()) return false

        val previousOffset = reading.offset - 1
        readInformationRepository.setLastReadOffset(previousOffset)
        return true
    }
}
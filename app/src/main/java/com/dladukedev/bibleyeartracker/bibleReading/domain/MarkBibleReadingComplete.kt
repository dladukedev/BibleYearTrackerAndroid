package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface MarkBibleReadingComplete {
    suspend operator fun invoke(bibleReadingId: Int): Boolean
}

class MarkBibleReadingCompleteUseCase @Inject constructor(
    private val bibleDataRepository: BibleDataRepository,
    private val readInformationRepository: ReadInformationRepository,
    private val canMarkBibleReadingComplete: CanMarkBibleReadingComplete,
) : MarkBibleReadingComplete {
    override suspend fun invoke(bibleReadingId: Int): Boolean {
        val reading = bibleDataRepository.getReading(bibleReadingId)

        if (reading == null) return false

        val isValidToComplete = canMarkBibleReadingComplete(reading)

        if (isValidToComplete.not()) return false

        readInformationRepository.setLastReadOffset(reading.offset)
        return true
    }
}
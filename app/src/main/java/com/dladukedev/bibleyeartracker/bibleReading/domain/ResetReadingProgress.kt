package com.dladukedev.bibleyeartracker.bibleReading.domain

import javax.inject.Inject

interface ResetReadingProgress {
    suspend operator fun invoke()
}

class ResetReadingProgressUseCase @Inject constructor(
    private val readInformationRepository: ReadInformationRepository,
): ResetReadingProgress {
    override suspend fun invoke() {
        readInformationRepository.setLastReadOffset(null)
    }
}

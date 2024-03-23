package com.dladukedev.core.schedule

import com.dladukedev.data.progress.ProgressOffsetRepository
import com.dladukedev.data.progress.ProgressStartDateRepository
import javax.inject.Inject

interface MarkBibleReadingComplete {
    suspend operator fun invoke(): Boolean
}

internal class MarkBibleReadingCompleteUseCase @Inject constructor(
    private val progressOffsetRepository: ProgressOffsetRepository,
) : MarkBibleReadingComplete {
    override suspend fun invoke(): Boolean {
        return try {
            progressOffsetRepository.incrementLastReadOffset()
            true
        } catch (e: Exception) {
            false
        }
    }
}
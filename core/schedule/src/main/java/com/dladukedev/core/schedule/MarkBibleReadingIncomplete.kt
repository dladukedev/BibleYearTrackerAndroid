package com.dladukedev.core.schedule

import com.dladukedev.data.progress.ProgressOffsetRepository
import javax.inject.Inject

interface MarkBibleReadingIncomplete {
    suspend operator fun invoke(): Boolean
}

internal class MarkBibleReadingIncompleteUseCase @Inject constructor(
    private val progressOffsetRepository: ProgressOffsetRepository,
) : MarkBibleReadingIncomplete {
    override suspend fun invoke(): Boolean {
        return try {
            progressOffsetRepository.decrementLastReadOffset()
            true
        } catch (e: Exception) {
            false
        }
    }
}
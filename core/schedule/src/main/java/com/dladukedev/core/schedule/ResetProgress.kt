package com.dladukedev.core.schedule

import com.dladukedev.data.progress.ProgressOffsetRepository
import javax.inject.Inject
import javax.inject.Singleton

interface ResetProgress {
    suspend operator fun invoke()
}

@Singleton
internal class ResetProgressUseCase @Inject constructor(
    private val progressOffsetRepository: ProgressOffsetRepository,
): ResetProgress {
    override suspend fun invoke() {
        progressOffsetRepository.clearLastReadOffset()
    }
}
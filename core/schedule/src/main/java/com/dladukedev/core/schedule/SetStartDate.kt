package com.dladukedev.core.schedule

import com.dladukedev.data.progress.ProgressStartDateRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface SetStartDate {
    suspend operator fun invoke(startDate: LocalDate)
}

@Singleton
internal class SetStartDateUseCase @Inject constructor(
    private val progressStartDateRepository: ProgressStartDateRepository,
): SetStartDate {
    override suspend fun invoke(startDate: LocalDate) {
        progressStartDateRepository.setStartDate(startDate)
    }
}

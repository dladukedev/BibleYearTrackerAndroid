package com.dladukedev.bibleyeartracker.settings.domain

import java.time.LocalDate
import javax.inject.Inject

interface EditStartDate {
    suspend operator fun invoke(startDate: LocalDate)
}

class EditStartDateUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
): EditStartDate {
    override suspend fun invoke(startDate: LocalDate) {
        settingsRepository.setStartDate(startDate)
    }
}
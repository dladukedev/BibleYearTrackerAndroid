package com.dladukedev.bibleyeartracker.settings.domain

import javax.inject.Inject

interface EditTheme {
    suspend operator fun invoke(theme: Theme)
}

class EditThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : EditTheme {
    override suspend fun invoke(theme: Theme) {
        settingsRepository.setTheme(theme)
    }

}
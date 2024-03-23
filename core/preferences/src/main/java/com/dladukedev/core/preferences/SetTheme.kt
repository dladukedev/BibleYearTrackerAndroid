package com.dladukedev.core.preferences

import com.dladukedev.common.models.Theme
import javax.inject.Inject
import javax.inject.Singleton

interface SetTheme {
    suspend operator fun invoke(theme: Theme)
}

@Singleton
internal class SetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
): SetTheme {
    override suspend fun invoke(theme: Theme) {
        settingsRepository.setTheme(theme)
    }
}
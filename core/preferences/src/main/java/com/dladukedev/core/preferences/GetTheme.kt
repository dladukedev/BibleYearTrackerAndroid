package com.dladukedev.core.preferences

import com.dladukedev.common.models.Theme
import javax.inject.Inject
import javax.inject.Singleton

interface GetTheme {
    suspend operator fun invoke(): Theme
}

@Singleton
internal class GetThemeUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
): GetTheme {
    override suspend fun invoke(): Theme {
        return settingsRepository.getTheme()
    }

}
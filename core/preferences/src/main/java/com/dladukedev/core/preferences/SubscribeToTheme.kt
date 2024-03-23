package com.dladukedev.core.preferences

import com.dladukedev.common.models.Theme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SubscribeToTheme {
    val values: Flow<Theme>
}

internal class SubscribeToThemeUseCase @Inject constructor(
    settingsRepository: SettingsRepository,
): SubscribeToTheme {
    override val values = settingsRepository.theme
}

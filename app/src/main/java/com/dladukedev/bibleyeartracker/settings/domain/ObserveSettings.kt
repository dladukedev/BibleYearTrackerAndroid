package com.dladukedev.bibleyeartracker.settings.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveSettings {
    operator fun invoke(): Flow<AppSettings>
}

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
): ObserveSettings {
    override fun invoke(): Flow<AppSettings> {
        return settingsRepository.observeSettings()
    }
}
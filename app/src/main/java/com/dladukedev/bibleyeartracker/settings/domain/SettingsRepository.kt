package com.dladukedev.bibleyeartracker.settings.domain

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SettingsRepository {
    fun observeStartDate(): Flow<LocalDate>
    suspend fun setStartDate(date: LocalDate)
    fun observeSettings(): Flow<AppSettings>
    suspend fun setTheme(theme: Theme)
    fun observeTheme(): Flow<Theme>
    suspend fun getTheme(): Theme
}
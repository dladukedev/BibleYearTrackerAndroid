package com.dladukedev.bibleyeartracker.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.SettingsRepository
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject


class SettingsDataStoreRepository @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
) : SettingsRepository {
    override fun observeStartDate(): Flow<LocalDate> {
        return settingsLocalDataSource.observeStartDate()
    }

    override suspend fun setStartDate(date: LocalDate) {
        settingsLocalDataSource.setStartDate(date)
    }

    override fun observeSettings(): Flow<AppSettings> {
        return settingsLocalDataSource.observeSettings()
    }

    override suspend fun setTheme(theme: Theme) {
        settingsLocalDataSource.setTheme(theme)
    }

    override fun observeTheme(): Flow<Theme> {
        return settingsLocalDataSource.observeTheme()
    }

    override suspend fun getTheme(): Theme {
        return settingsLocalDataSource.getTheme()
    }
}
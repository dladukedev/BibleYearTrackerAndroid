package com.dladukedev.bibleyeartracker.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dladukedev.bibleyeartracker.common.GetCurrentDate
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject


val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface SettingsLocalDataSource {
    fun observeStartDate(): Flow<LocalDate>
    suspend fun setStartDate(date: LocalDate)
    fun observeSettings(): Flow<AppSettings>
    suspend fun setTheme(theme: Theme)
    fun observeTheme(): Flow<Theme>
    suspend fun getTheme(): Theme
}

class SettingLocalDataStoreSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentDate: GetCurrentDate,
) : SettingsLocalDataSource {
    override fun observeStartDate(): Flow<LocalDate> {
        return context.settingsDataStore.data.map { data ->
            data.startDate()
        }
    }

    override suspend fun setStartDate(date: LocalDate) {
        context.settingsDataStore.edit { data ->
            val newDate = date.toEpochDay()
            data[startDateKey] = newDate
        }
    }

    override fun observeSettings(): Flow<AppSettings> {
        return context.settingsDataStore.data.map { data ->
            AppSettings(
                readingGoalStartDate = data.startDate(),
                theme = data.theme(),
            )
        }
    }

    override suspend fun setTheme(theme: Theme) {
        context.settingsDataStore.edit { data ->
            val newTheme = theme.asInt()
            data[themeKey] = newTheme
        }
    }

    override fun observeTheme(): Flow<Theme> {
        return context.settingsDataStore.data.map { data ->
            data.theme()
        }
    }

    override suspend fun getTheme(): Theme {
        return observeTheme().first()
    }

    private val startDateKey = longPreferencesKey("startDate")
    private val themeKey = intPreferencesKey("theme")

    private fun Preferences.startDate(): LocalDate {
        return this[startDateKey]?.let { startDateLong ->
            LocalDate.ofEpochDay(startDateLong)
        } ?: getCurrentDate()
    }

    private fun Preferences.theme(): Theme {
        return when (this[themeKey]) {
            DarkThemeIndex -> Theme.DARK
            LightThemeIndex -> Theme.LIGHT
            else -> Theme.SYSTEM
        }
    }

    private companion object {
        const val SystemThemeIndex = 0
        const val DarkThemeIndex = 1
        const val LightThemeIndex = 2
    }

    private fun Theme.asInt(): Int {
        return when (this) {
            Theme.SYSTEM -> SystemThemeIndex
            Theme.DARK -> DarkThemeIndex
            Theme.LIGHT -> LightThemeIndex
        }
    }
}
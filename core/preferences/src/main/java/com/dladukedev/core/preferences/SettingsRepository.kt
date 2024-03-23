package com.dladukedev.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dladukedev.common.models.Theme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


internal interface SettingsRepository {
    suspend fun setTheme(theme: Theme)
    val theme: Flow<Theme>
    suspend fun getTheme(): Theme
}

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal class SettingLocalRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    override suspend fun setTheme(theme: Theme) {
        context.settingsDataStore.edit { data ->
            val newTheme = theme.asInt()
            data[themeKey] = newTheme
        }
    }

    override val theme = context.settingsDataStore.data.map { data ->
            data.theme()
    }

    override suspend fun getTheme(): Theme {
        return theme.first()
    }

    private val themeKey = intPreferencesKey("theme")

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
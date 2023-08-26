package com.dladukedev.bibleyeartracker.settings.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class EditThemeTest {
    @Test
    fun `updates the theme in the repository`() = runTest {
        // Given
        val settingsRepository = object : SettingsRepository {
            var currentTheme: Theme? = null
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) = TODO("Not yet implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) {
                currentTheme = theme
            }

            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }
        val editTheme = EditThemeUseCase(settingsRepository)
        val expected = Theme.DARK

        // When
        editTheme(Theme.DARK)
        val actual =  settingsRepository.currentTheme

        // Then
        Assert.assertEquals(expected, actual)
    }
}
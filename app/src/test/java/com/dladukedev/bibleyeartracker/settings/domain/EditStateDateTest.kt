package com.dladukedev.bibleyeartracker.settings.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class EditStateDateTest {
    @Test
    fun `updates the date in the repository`() = runTest {
        // Given
        val settingsRepository = object : SettingsRepository {
            var currentLocalDate: LocalDate? = null
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) {
                currentLocalDate = date
            }
            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }
        val editStartDate = EditStartDateUseCase(settingsRepository)
        val expected = LocalDate.of(2000, Month.JANUARY, 1)

        // When
        editStartDate(LocalDate.of(2000, Month.JANUARY, 1))
        val actual =  settingsRepository.currentLocalDate

        // Then
        Assert.assertEquals(expected, actual)
    }
}
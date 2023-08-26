package com.dladukedev.bibleyeartracker.settings.domain

import app.cash.turbine.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class ObserveSettingsTest {
    @Test
    fun `emits expected values`() = runTest {
        // Given
        val settingsRepository = object : SettingsRepository {
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) = TODO("Not yet implemented")
            override fun observeSettings(): Flow<AppSettings> = flowOf(
                AppSettings(LocalDate.of(2000, Month.JANUARY, 1), Theme.SYSTEM),
                AppSettings(LocalDate.of(2000, Month.JANUARY, 2), Theme.LIGHT),
                AppSettings(LocalDate.of(2000, Month.JANUARY, 3), Theme.DARK),
            ).onEach { delay(1) }

            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }
        val observeSettings = ObserveSettingsUseCase(settingsRepository)

        // When
        observeSettings().test {
            // Then
            val actual1 = awaitItem()
            val expected1 = AppSettings(LocalDate.of(2000, Month.JANUARY, 1), Theme.SYSTEM)
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = AppSettings(LocalDate.of(2000, Month.JANUARY, 2), Theme.LIGHT)
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = AppSettings(LocalDate.of(2000, Month.JANUARY, 3), Theme.DARK)
            Assert.assertEquals(expected3, actual3)

            awaitComplete()
        }
    }
}
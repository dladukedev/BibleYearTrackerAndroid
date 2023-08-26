package com.dladukedev.bibleyeartracker.settings.data

import app.cash.turbine.test
import com.dladukedev.bibleyeartracker.settings.domain.AppSettings
import com.dladukedev.bibleyeartracker.settings.domain.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.Month

class SettingsDataStoreRepositoryTest {
    @Test
    fun `observeStateDate emits expected values`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
            override fun observeStartDate(): Flow<LocalDate> = flowOf(
                LocalDate.of(2000, Month.JANUARY, 1),
                LocalDate.of(2000, Month.JANUARY, 2),
                LocalDate.of(2000, Month.JANUARY, 3),
            ).onEach { delay(1) }

            override suspend fun setStartDate(date: LocalDate) = TODO("Not yet implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }
        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)

        // When
        settingsDataStoreRepository.observeStartDate().test {
            // Then
            val actual1 = awaitItem()
            val expected1 = LocalDate.of(2000, Month.JANUARY, 1)
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = LocalDate.of(2000, Month.JANUARY, 2)
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = LocalDate.of(2000, Month.JANUARY, 3)
            Assert.assertEquals(expected3, actual3)

            awaitComplete()
        }
    }

    @Test
    fun `setStartDate updates the start date`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
            var currentStartDate: LocalDate? = null
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) {
                currentStartDate = date
            }

            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }

        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)
        val expected = LocalDate.of(2000, Month.JANUARY, 1)

        // When
        settingsDataStoreRepository.setStartDate(LocalDate.of(2000, Month.JANUARY, 1))
        val actual = settingsLocalDataSource.currentStartDate

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `observeSettings emits expected values`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
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
        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)

        // When
        settingsDataStoreRepository.observeSettings().test {
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

    @Test
    fun `setTheme updates the theme`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
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

        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)
        val expected = Theme.DARK

        // When
        settingsDataStoreRepository.setTheme(Theme.DARK)
        val actual = settingsLocalDataSource.currentTheme

        // Then
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun `observeTheme emits expected values`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) = TODO("Not yet implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> =
                flowOf(Theme.SYSTEM, Theme.LIGHT, Theme.DARK).onEach { delay(1) }

            override suspend fun getTheme(): Theme = TODO("Not yet implemented")
        }
        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)

        // When
        settingsDataStoreRepository.observeTheme().test {
            // Then
            val actual1 = awaitItem()
            val expected1 = Theme.SYSTEM
            Assert.assertEquals(expected1, actual1)

            // Then
            val actual2 = awaitItem()
            val expected2 = Theme.LIGHT
            Assert.assertEquals(expected2, actual2)

            // Then
            val actual3 = awaitItem()
            val expected3 = Theme.DARK
            Assert.assertEquals(expected3, actual3)

            awaitComplete()
        }
    }

    @Test
    fun `getTheme returns the current theme`() = runTest {
        // Given
        val settingsLocalDataSource = object : SettingsLocalDataSource {
            override fun observeStartDate(): Flow<LocalDate> = TODO("Not yet implemented")
            override suspend fun setStartDate(date: LocalDate) = TODO("Not yet implemented")
            override fun observeSettings(): Flow<AppSettings> = TODO("Not yet implemented")
            override suspend fun setTheme(theme: Theme) = TODO("Not yet implemented")
            override fun observeTheme(): Flow<Theme> = TODO("Not yet implemented")
            override suspend fun getTheme(): Theme = Theme.DARK
        }
        val settingsDataStoreRepository = SettingsDataStoreRepository(settingsLocalDataSource)
        val expected = Theme.DARK

        // When
        val actual = settingsDataStoreRepository.getTheme()

        // Then
        Assert.assertEquals(expected, actual)
    }
}
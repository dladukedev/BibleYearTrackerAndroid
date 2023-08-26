package com.dladukedev.bibleyeartracker.di

import com.dladukedev.bibleyeartracker.bibleReading.data.BibleBookMapper
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleBookMapperImpl
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleDataCacheDataSource
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleDataInMemoryCacheDataSource
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleDataLocalDataSource
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleDataLocalRepository
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleDataRawResDataSource
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleReadingParser
import com.dladukedev.bibleyeartracker.bibleReading.data.BibleReadingsParserImpl
import com.dladukedev.bibleyeartracker.bibleReading.data.ReadInformationDataStoreRepository
import com.dladukedev.bibleyeartracker.bibleReading.data.ReadInformationLocalDataSource
import com.dladukedev.bibleyeartracker.bibleReading.data.ReadInformationLocalDataStoreSource
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleDataRepository
import com.dladukedev.bibleyeartracker.bibleReading.domain.CanMarkBibleReadingComplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.CanMarkBibleReadingCompleteUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.CanMarkBibleReadingIncomplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.CanMarkBibleReadingIncompleteUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingComplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingCompleteUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingIncomplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.MarkBibleReadingIncompleteUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.ReadInformationRepository
import com.dladukedev.bibleyeartracker.bibleReading.domain.ResetReadingProgress
import com.dladukedev.bibleyeartracker.bibleReading.domain.ResetReadingProgressUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToBibleReadings
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToBibleReadingsUseCase
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToPercentComplete
import com.dladukedev.bibleyeartracker.bibleReading.domain.SubscribeToPercentCompleteUseCase
import com.dladukedev.bibleyeartracker.common.FormatDate
import com.dladukedev.bibleyeartracker.common.FormatDateUseCase
import com.dladukedev.bibleyeartracker.common.GenerateID
import com.dladukedev.bibleyeartracker.common.GenerateUUIDUseCase
import com.dladukedev.bibleyeartracker.common.GetCurrentDate
import com.dladukedev.bibleyeartracker.common.GetCurrentDateUseCase
import com.dladukedev.bibleyeartracker.settings.data.SettingLocalDataStoreSource
import com.dladukedev.bibleyeartracker.settings.data.SettingsDataStoreRepository
import com.dladukedev.bibleyeartracker.settings.data.SettingsLocalDataSource
import com.dladukedev.bibleyeartracker.settings.domain.ObserveSettings
import com.dladukedev.bibleyeartracker.settings.domain.ObserveSettingsUseCase
import com.dladukedev.bibleyeartracker.settings.domain.SettingsRepository
import com.dladukedev.bibleyeartracker.settings.domain.EditStartDate
import com.dladukedev.bibleyeartracker.settings.domain.EditStartDateUseCase
import com.dladukedev.bibleyeartracker.settings.domain.EditTheme
import com.dladukedev.bibleyeartracker.settings.domain.EditThemeUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModule {
    @Binds
    abstract fun provideSubscribeToBibleReadings(provides: SubscribeToBibleReadingsUseCase): SubscribeToBibleReadings

    @Binds
    abstract fun provideFormatDate(provides: FormatDateUseCase): FormatDate

    @Binds
    abstract fun provideGetCurrentDate(useCase: GetCurrentDateUseCase): GetCurrentDate

    @Binds
    abstract fun provideBibleDataRepository(repository: BibleDataLocalRepository): BibleDataRepository

    @Binds
    abstract fun provideReadInformationRepository(repository: ReadInformationDataStoreRepository): ReadInformationRepository

    @Binds
    abstract fun provideSettingsRepository(repository: SettingsDataStoreRepository): SettingsRepository

    @Binds
    abstract fun bindBibleDataCacheDataSource(dataSource: BibleDataInMemoryCacheDataSource): BibleDataCacheDataSource

    @Binds
    abstract fun bindBibleDataLocalDataSource(dataSource: BibleDataRawResDataSource): BibleDataLocalDataSource

    @Binds
    abstract fun bindSettingsLocalDataSource(dataSource: SettingLocalDataStoreSource): SettingsLocalDataSource

    @Binds
    abstract fun bindReadInformationLocalDataSource(dataSource: ReadInformationLocalDataStoreSource): ReadInformationLocalDataSource

    @Binds
    abstract fun bindMarkBibleReadingCompleteUseCase(useCase: MarkBibleReadingCompleteUseCase): MarkBibleReadingComplete

    @Binds
    abstract fun bindCanMarkBibleReadingComplete(useCase: CanMarkBibleReadingCompleteUseCase): CanMarkBibleReadingComplete

    @Binds
    abstract fun bindGenerateUUID(useCase: GenerateUUIDUseCase): GenerateID

    @Binds
    abstract fun bindMarkBibleReadingIncompleteUseCase(useCase: MarkBibleReadingIncompleteUseCase): MarkBibleReadingIncomplete

    @Binds
    abstract fun bindCanMarkBibleReadingIncomplete(useCase: CanMarkBibleReadingIncompleteUseCase): CanMarkBibleReadingIncomplete

    @Binds
    abstract fun bindSubscribeToPercentComplete(useCase: SubscribeToPercentCompleteUseCase): SubscribeToPercentComplete

    @Binds
    abstract fun bindObserverSettings(useCase: ObserveSettingsUseCase): ObserveSettings

    @Binds
    abstract fun bindUpdateStartDate(useCase: EditStartDateUseCase): EditStartDate

    @Binds
    abstract fun bindEditTheme(useCase: EditThemeUseCase): EditTheme

    @Binds
    abstract fun bindResetReadingProgress(useCase: ResetReadingProgressUseCase): ResetReadingProgress

    @Binds
    abstract fun bindBibleReadingParser(parser: BibleReadingsParserImpl): BibleReadingParser

    @Binds
    abstract fun bindBibleBookMapper(mapper: BibleBookMapperImpl): BibleBookMapper
}
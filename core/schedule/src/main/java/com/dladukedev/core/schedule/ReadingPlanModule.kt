package com.dladukedev.core.schedule

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ReadingPlanModule {
    @Binds abstract fun bindSetReadingPlan(useCase: SetReadingPlanUseCase): SetReadingPlan
    @Binds abstract fun bindSetStartDate(useCase: SetStartDateUseCase): SetStartDate
    @Binds abstract fun bindSubscribeToStartDate(useCase: SubscribeToStartDateUseCase): SubscribeToStartDate
    @Binds abstract fun bindSubscribeToBibleReadings(useCase: SubscribeToBibleReadingsUseCase): SubscribeToBibleReadings
    @Binds abstract fun bindMarkBibleReadingComplete(useCase: MarkBibleReadingCompleteUseCase): MarkBibleReadingComplete
    @Binds abstract fun bindMarkBibleReadingIncomplete(useCase: MarkBibleReadingIncompleteUseCase): MarkBibleReadingIncomplete
    @Binds abstract fun bindResetProgress(useCase: ResetProgressUseCase): ResetProgress
}

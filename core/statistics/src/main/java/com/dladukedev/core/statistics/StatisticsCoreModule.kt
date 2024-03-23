package com.dladukedev.core.statistics

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class StatisticsCoreModule {
    @Binds abstract fun bindSubscribeToBibleBooksReadStatus(useCase: SubscribeBibleBooksReadStateUseCase): SubscribeBibleBooksReadState
    @Binds abstract fun bindSubscribeToStats(useCase: SubscribeToStatsUseCase): SubscribeToStats
}
package com.dladukedev.data.progress

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ProgressModule {
    @Binds abstract fun bindProgressOffsetRepository(repo: ProgressOffsetRepositoryImpl): ProgressOffsetRepository
    @Binds abstract fun bindProgressStartDateRepository(repo: ProgressStartDateRepositoryImpl): ProgressStartDateRepository
}

package com.dladukedev.data.plan

import com.dladukedev.data.plan.datasources.CachedInMemoryReadingSetsDataSource
import com.dladukedev.data.plan.datasources.CachedReadingSetsDataSource
import com.dladukedev.data.plan.datasources.ReadingsDataSource
import com.dladukedev.data.plan.datasources.ReadingsRawDataSource
import com.dladukedev.data.plan.datasources.SelectedPlanDataSource
import com.dladukedev.data.plan.datasources.TodoSelectedPlanDataSource
import com.dladukedev.data.plan.parseplan.BibleBookMapper
import com.dladukedev.data.plan.parseplan.BibleBookMapperImpl
import com.dladukedev.data.plan.parseplan.BibleReadingParser
import com.dladukedev.data.plan.parseplan.BibleReadingsParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
internal abstract class PlanDataModule {
    @Binds abstract fun bindBibleBookMapperImpl(mapper: BibleBookMapperImpl): BibleBookMapper
    @Binds abstract fun bindBibleReadingParser(parserImpl: BibleReadingsParserImpl): BibleReadingParser


    @Binds abstract fun bindCachedReadingSetsDataSource(source: CachedInMemoryReadingSetsDataSource): CachedReadingSetsDataSource
    @Binds abstract fun bindReadingsDataSource(source: ReadingsRawDataSource): ReadingsDataSource
    @Binds abstract fun bindSelectedPlanDataSource(source: TodoSelectedPlanDataSource): SelectedPlanDataSource

    @Binds abstract fun bindBibleDataRepository(repo: PlanLocalRepository): PlanRepository
}

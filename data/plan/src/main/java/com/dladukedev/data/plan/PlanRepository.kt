package com.dladukedev.data.plan

import com.dladukedev.common.models.ReadingPlan
import com.dladukedev.common.models.ReadingPlanKey
import com.dladukedev.data.plan.datasources.CachedInMemoryReadingSetsDataSource
import com.dladukedev.data.plan.datasources.ReadingsDataSource
import com.dladukedev.data.plan.datasources.SelectedPlanDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface PlanRepository {
    val selectedPlan: Flow<ReadingPlan>

    suspend fun setSelectedPlan(key: ReadingPlanKey)
}

internal class PlanLocalRepository @Inject constructor(
    private val selectedPlanDataSource: SelectedPlanDataSource,
    private val cachedReadingsDataSource: CachedInMemoryReadingSetsDataSource,
    private val readingsDataSource: ReadingsDataSource,
) : PlanRepository {
    private suspend fun loadReadingPlan(key: ReadingPlanKey): ReadingPlan {
        cachedReadingsDataSource.getCachedReadingPlan(key)?.let { return it }

        val plan = readingsDataSource.getReadingPlanForKey(key)
        cachedReadingsDataSource.setCachedReadingPlan(plan)
        return plan
    }

    override val selectedPlan = selectedPlanDataSource.selectedPlan
        .map { key -> loadReadingPlan(key) }

    override suspend fun setSelectedPlan(key: ReadingPlanKey) {
        selectedPlanDataSource.setPlan(key)
    }
}
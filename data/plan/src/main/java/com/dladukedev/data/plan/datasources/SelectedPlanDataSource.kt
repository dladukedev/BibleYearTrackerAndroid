package com.dladukedev.data.plan.datasources

import com.dladukedev.common.models.ReadingPlanKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface SelectedPlanDataSource {
    val selectedPlan: Flow<ReadingPlanKey>
    suspend fun setPlan(plan: ReadingPlanKey)
}

class TodoSelectedPlanDataSource @Inject constructor(): SelectedPlanDataSource {
    private val _selectedPlan = MutableStateFlow(ReadingPlanKey.BibleInAYear)
    override val selectedPlan= _selectedPlan

    override suspend fun setPlan(plan: ReadingPlanKey) {
        _selectedPlan.update { plan }
    }
}


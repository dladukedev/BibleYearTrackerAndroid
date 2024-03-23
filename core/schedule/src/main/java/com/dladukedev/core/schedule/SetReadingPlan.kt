package com.dladukedev.core.schedule

import com.dladukedev.common.models.ReadingPlanKey
import com.dladukedev.data.plan.PlanRepository
import com.dladukedev.data.progress.ProgressOffsetRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

interface SetReadingPlan {
    suspend operator fun invoke(planKey: ReadingPlanKey)
}

@Singleton
internal class SetReadingPlanUseCase @Inject constructor(
    private val planRepository: PlanRepository,
    private val progressOffsetRepository: ProgressOffsetRepository,
): SetReadingPlan {
    override suspend fun invoke(planKey: ReadingPlanKey) {
        planRepository.setSelectedPlan(planKey)

        progressOffsetRepository.clearLastReadOffset()

        val newPlan = planRepository.selectedPlan.first()
        val maxOffset = newPlan.readingSets.lastIndex

        progressOffsetRepository.setMaxOffset(maxOffset)
    }
}
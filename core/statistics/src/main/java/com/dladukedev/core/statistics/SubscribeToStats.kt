package com.dladukedev.core.statistics

import com.dladukedev.data.plan.PlanRepository
import com.dladukedev.data.progress.ProgressOffsetRepository
import com.dladukedev.data.progress.ProgressStartDateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SubscribeToStats {
    val values: Flow<Stats>
}

internal class SubscribeToStatsUseCase @Inject constructor(
    planRepository: PlanRepository,
    progressOffsetRepository: ProgressOffsetRepository,
    progressStartDateRepository: ProgressStartDateRepository,
) : SubscribeToStats {
    private val bibleReadingsSizeFlow = planRepository.selectedPlan.map { it.readingSets.size }
    private val readCountFlow = progressOffsetRepository.lastReadOffset.map { (it ?: -1) + 1 }

    private val percentCompleteFlow = combine(
        readCountFlow,
        bibleReadingsSizeFlow,
    ) { readCount, bibleReadingsSize ->
        (readCount.toDouble() / bibleReadingsSize.toDouble()) * 100
    }

    private val daysRemainingFlow = combine(
        readCountFlow,
        bibleReadingsSizeFlow,
    ) { readCount, bibleReadingsSize ->
        bibleReadingsSize - readCount
    }

    private val daysOffTargetFlow = combine(
        progressOffsetRepository.lastReadOffset,
        progressStartDateRepository.currentDateOffset,
    ) { lastReadOffset, currentDateOffset ->
        (lastReadOffset ?: -1) - currentDateOffset
    }

    override val values = combine(
        percentCompleteFlow,
        daysRemainingFlow,
        daysOffTargetFlow,
        bibleReadingsSizeFlow,
        readCountFlow,
    ) {  percentComplete, daysRemaining, daysOffTarget, bibleReadingsSize, daysComplete ->
        Stats(
            percentComplete = percentComplete,
            daysRemaining = daysRemaining,
            daysComplete = daysComplete,
            daysTotal = bibleReadingsSize,
            daysOffTarget = daysOffTarget,
        )
    }
}

data class Stats(
    val percentComplete: Double,
    val daysComplete: Int,
    val daysRemaining: Int,
    val daysTotal: Int,
    val daysOffTarget: Int, // negative is behind, positive is ahead, 0 is on track
)
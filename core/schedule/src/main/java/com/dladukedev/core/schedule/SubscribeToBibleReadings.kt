package com.dladukedev.core.schedule

import com.dladukedev.common.models.BibleReadingItem
import com.dladukedev.common.models.ReadingSet
import com.dladukedev.data.plan.PlanRepository
import com.dladukedev.data.progress.ProgressOffsetRepository
import com.dladukedev.data.progress.ProgressStartDateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import javax.inject.Inject

interface SubscribeToBibleReadings {
    val values: Flow<List<BibleReadingItem>>
}

internal class SubscribeToBibleReadingsUseCase @Inject constructor(
    planRepository: PlanRepository,
    progressStartDateRepository: ProgressStartDateRepository,
    progressOffsetRepository: ProgressOffsetRepository,
) : SubscribeToBibleReadings {
    override val values = combine(
            planRepository.selectedPlan,
            progressStartDateRepository.startDate,
            progressOffsetRepository.lastReadOffset,
            progressStartDateRepository.currentDateOffset,
        ) { plan, startDate, lastReadOffset, currentDateOffset ->
            plan.readingSets.mapIndexed { index, reading ->
                mapToBibleReadingItem(reading, index, startDate, lastReadOffset, currentDateOffset)
            }
    }

    private fun mapToBibleReadingItem(
        reading: ReadingSet,
        offset: Int,
        startDate: LocalDate,
        lastReadOffset: Int?,
        currentDateOffset: Int,
    ): BibleReadingItem {
        val readingPlannedDate = startDate.plusDays(offset.toLong())
        val isRead = when (lastReadOffset) {
            null -> false // No Items Yet Read
            else -> offset <= lastReadOffset
        }
        val isToday = offset == currentDateOffset

        return BibleReadingItem(
            id = offset,
            date = readingPlannedDate,
            readings = reading,
            isMarkedComplete = isRead,
            isToday = isToday,
        )
    }
}
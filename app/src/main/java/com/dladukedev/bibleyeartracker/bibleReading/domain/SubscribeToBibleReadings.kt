package com.dladukedev.bibleyeartracker.bibleReading.domain

import com.dladukedev.bibleyeartracker.common.GetCurrentDate
import com.dladukedev.bibleyeartracker.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface SubscribeToBibleReadings {
    operator fun invoke(): Flow<List<BibleReadingItem>>
}

class SubscribeToBibleReadingsUseCase @Inject constructor(
    private val getCurrentDate: GetCurrentDate,
    private val bibleDataRepository: BibleDataRepository,
    private val readInformationRepository: ReadInformationRepository,
    private val settingsRepository: SettingsRepository,
) : SubscribeToBibleReadings {
    override operator fun invoke(): Flow<List<BibleReadingItem>> {
        val bibleReadingsFlow = flow {
            val readings = bibleDataRepository.getBibleReadings()
            emit(readings)
        }

        val startDateFlow = settingsRepository.observeStartDate()

        val lastReadOffsetFlow = readInformationRepository.subscribeGetLastReadOffset()

        return combine(
            bibleReadingsFlow,
            startDateFlow,
            lastReadOffsetFlow
        ) { bibleReadings, startDate, lastReadOffset ->
            val today = getCurrentDate()

            val currentDateOffset = ChronoUnit.DAYS.between(startDate, today)

            bibleReadings.map { reading ->
                mapToBibleReadingItem(reading, startDate, lastReadOffset, currentDateOffset)
            }
        }
    }

    private fun mapToBibleReadingItem(
        reading: BibleReadingDay,
        startDate: LocalDate,
        lastReadOffset: Int?,
        currentDateOffset: Long,
    ): BibleReadingItem {
        val readingDateOffset = reading.offset.toLong()
        val readingPlannedDate = startDate.plusDays(readingDateOffset)
        val isRead = when (lastReadOffset) {
            null -> false // No Items Yet Read
            else -> reading.offset <= lastReadOffset
        }
        val isToday = readingDateOffset == currentDateOffset

        return BibleReadingItem(
            id = reading.id,
            date = readingPlannedDate,
            readings = reading.readings,
            isMarkedComplete = isRead,
            isToday = isToday,
        )
    }
}
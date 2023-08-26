package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface SubscribeToPercentComplete {
    operator fun invoke(): Flow<Double>
}

class SubscribeToPercentCompleteUseCase @Inject constructor(
    private val bibleDataRepository: BibleDataRepository,
    private val readInformationRepository: ReadInformationRepository,
) : SubscribeToPercentComplete {
    override fun invoke(): Flow<Double> {
        val bibleReadingsSizeFlow = flow {
            val readings = bibleDataRepository.getBibleReadings()
            emit(readings.size)
        }

        val lastReadOffsetFlow = readInformationRepository.subscribeGetLastReadOffset()

        return combine(
            bibleReadingsSizeFlow,
            lastReadOffsetFlow
        ) { bibleReadingsSize, lastReadOffset ->
            val readCount = (lastReadOffset ?: -1) + 1

            (readCount.toDouble() / bibleReadingsSize.toDouble()) * 100
        }
    }

}
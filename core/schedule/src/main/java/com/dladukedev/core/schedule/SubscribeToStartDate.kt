package com.dladukedev.core.schedule

import com.dladukedev.data.progress.ProgressStartDateRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface SubscribeToStartDate {
    val values: Flow<LocalDate>
}

@Singleton
internal class SubscribeToStartDateUseCase @Inject constructor(
    progressStartDateRepository: ProgressStartDateRepository
): SubscribeToStartDate {
    override val values: Flow<LocalDate> = progressStartDateRepository.startDate
}
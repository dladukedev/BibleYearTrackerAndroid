package com.dladukedev.bibleyeartracker.bibleReading.domain

import kotlinx.coroutines.flow.Flow

interface ReadInformationRepository {
    fun subscribeGetLastReadOffset(): Flow<Int?>
    suspend fun getLastReadOffset(): Int?
    suspend fun setLastReadOffset(lastReadOffset: Int?)
}
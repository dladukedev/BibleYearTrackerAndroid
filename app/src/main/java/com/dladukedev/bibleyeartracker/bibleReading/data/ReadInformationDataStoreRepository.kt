package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.ReadInformationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadInformationDataStoreRepository @Inject constructor(
    private val readInformationLocalDataSource: ReadInformationLocalDataSource,
): ReadInformationRepository {
    override fun subscribeGetLastReadOffset(): Flow<Int?> {
        return readInformationLocalDataSource.subscribeLastReadOffset()
    }

    override suspend fun getLastReadOffset(): Int? {
        return readInformationLocalDataSource.getLastReadOffset()
    }

    override suspend fun setLastReadOffset(lastReadOffset: Int?) {
        readInformationLocalDataSource.setLastReadOffset(lastReadOffset)
    }
}
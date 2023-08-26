package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingDay
import javax.inject.Inject
import javax.inject.Singleton


interface BibleDataCacheDataSource {
   fun getBibleData(): List<BibleReadingDay>?
   fun setBibleData(bibleData: List<BibleReadingDay>)
}

@Singleton
class BibleDataInMemoryCacheDataSource @Inject constructor(): BibleDataCacheDataSource {
    private var _bibleData: List<BibleReadingDay>? = null

    override fun getBibleData(): List<BibleReadingDay>? {
        return _bibleData
    }

    override fun setBibleData(bibleData: List<BibleReadingDay>) {
        _bibleData = bibleData
    }
}
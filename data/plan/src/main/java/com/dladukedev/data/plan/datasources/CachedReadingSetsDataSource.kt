package com.dladukedev.data.plan.datasources

import com.dladukedev.common.models.ReadingPlan
import com.dladukedev.common.models.ReadingPlanKey
import javax.inject.Inject
import javax.inject.Singleton


interface CachedReadingSetsDataSource {
    fun getCachedReadingPlan(key: ReadingPlanKey): ReadingPlan?
    fun setCachedReadingPlan(readingPlan: ReadingPlan)
}

@Singleton
class CachedInMemoryReadingSetsDataSource @Inject constructor() : CachedReadingSetsDataSource {
    private val _bibleData = mutableMapOf<ReadingPlanKey, ReadingPlan>()

    override fun getCachedReadingPlan(key: ReadingPlanKey): ReadingPlan? {
        return _bibleData[key]
    }

    override fun setCachedReadingPlan(readingPlan: ReadingPlan) {
        _bibleData[readingPlan.key] = readingPlan
    }
}

package com.dladukedev.bibleyeartracker.bibleReading.data

import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleDataRepository
import com.dladukedev.bibleyeartracker.bibleReading.domain.BibleReadingDay
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class BibleDataLocalRepository @Inject constructor(
    private val cacheDataSource: BibleDataCacheDataSource,
    private val localDataSource: BibleDataLocalDataSource,
    private val bibleReadingParser: BibleReadingParser,
) : BibleDataRepository {
    override suspend fun getBibleReadings(): List<BibleReadingDay> {
        val cachedResults = cacheDataSource.getBibleData()
        if (cachedResults != null && cachedResults.isNotEmpty()) {
            return cachedResults
        }

        val results = localDataSource.getBibleData()
            .map {
                coroutineScope {
                    async {
                        val reading1 = async { bibleReadingParser.parse(it.reading1) }
                        val reading2 = async { bibleReadingParser.parse(it.reading2) }
                        val reading3 = async { bibleReadingParser.parse(it.reading3) }

                        BibleReadingDay(
                            id = it.id,
                            readings = listOf(
                                reading1.await(),
                                reading2.await(),
                                reading3.await(),
                            ).flatten(),
                            offset = it.offset
                        )
                    }
                }
            }
            .awaitAll()
            .sortedBy { it.offset }

        cacheDataSource.setBibleData(results)

        return results
    }

    override suspend fun getReading(readingId: Int): BibleReadingDay? {
        return getBibleReadings()
            .firstOrNull { reading ->
                reading.id == readingId
            }
    }
}
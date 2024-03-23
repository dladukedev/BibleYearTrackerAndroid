package com.dladukedev.data.plan.datasources

import android.content.Context
import androidx.annotation.RawRes
import com.dladukedev.common.models.ReadingPlan
import com.dladukedev.common.models.ReadingPlanKey
import com.dladukedev.common.models.ReadingSet
import com.dladukedev.data.plan.R
import com.dladukedev.data.plan.parseplan.BibleReadingParser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface ReadingsDataSource {
    suspend fun getReadingPlanForKey(key: ReadingPlanKey): ReadingPlan
}

class ReadingsRawDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bibleReadingParser: BibleReadingParser,
    ): ReadingsDataSource {
    override suspend fun getReadingPlanForKey(key: ReadingPlanKey): ReadingPlan {
        val resId = getResIdForPlan(key)
        val rawReadings = getRawReadingsFromFile(resId)
        val readingSets = getReadingSetsFromRawReadings(rawReadings)

        return ReadingPlan(
            key = key,
            readingSets = readingSets,
        )
    }

    @RawRes
    private fun getResIdForPlan(key: ReadingPlanKey): Int {
        return when(key) {
            ReadingPlanKey.BibleInAYear -> R.raw.bible_data
        }
    }

    private fun getRawReadingsFromFile(@RawRes resId: Int): List<String> {
        val json = context.resources.openRawResource(resId)
            .bufferedReader()
            .use { it.readText() }

        return Json.decodeFromString<BibleReadingsRaw>(json).readings
    }

    private suspend fun getReadingSetsFromRawReadings(readings: List<String>): List<ReadingSet> {
        return readings
            .map {readingsRaw ->
                coroutineScope {
                    async {
                        bibleReadingParser.parse(readingsRaw)
                    }
                }
            }
            .awaitAll()
    }

    @Serializable
    private data class BibleReadingsRaw(
        val readings: List<String>,
    )
}

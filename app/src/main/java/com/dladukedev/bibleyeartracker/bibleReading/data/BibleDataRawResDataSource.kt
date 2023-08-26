package com.dladukedev.bibleyeartracker.bibleReading.data

import android.content.Context
import com.dladukedev.bibleyeartracker.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface BibleDataLocalDataSource {
    suspend fun getBibleData(): List<BibleReadingDataModel>
}

class BibleDataRawResDataSource @Inject constructor(@ApplicationContext private val context: Context): BibleDataLocalDataSource{
    override suspend fun getBibleData(): List<BibleReadingDataModel> {
        val json = context.resources.openRawResource(R.raw.bible_data)
            .bufferedReader()
            .use { it.readText() }


        val root = Json.decodeFromString<BibleReadingRoot>(json)

        return root.days
    }
}
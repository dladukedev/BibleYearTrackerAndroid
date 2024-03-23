package com.dladukedev.data.progress

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.dladukedev.common.util.datetime.GetCurrentDate
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

interface ProgressStartDateRepository {
    val startDate: Flow<LocalDate>
    val currentDateOffset: Flow<Int>

    suspend fun setStartDate(date: LocalDate)
}

internal class ProgressStartDateRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCurrentDate: GetCurrentDate,
): ProgressStartDateRepository {


    override val startDate = context.progressDataStore.data.map { data ->
        data.startDate
    }

    override val currentDateOffset: Flow<Int> = startDate.map { start ->
        val today = getCurrentDate()

        ChronoUnit.DAYS.between(start, today).toInt()
    }


    override suspend fun setStartDate(date: LocalDate) {
        context.progressDataStore.edit { data ->
            val newDate = date.toEpochDay()
            data[startDateKey] = newDate
        }
    }


    private val startDateKey = longPreferencesKey("startDate")

    private val Preferences.startDate get(): LocalDate =
        this[startDateKey]?.let { startDateLong ->
            LocalDate.ofEpochDay(startDateLong)
        } ?: getCurrentDate()

}
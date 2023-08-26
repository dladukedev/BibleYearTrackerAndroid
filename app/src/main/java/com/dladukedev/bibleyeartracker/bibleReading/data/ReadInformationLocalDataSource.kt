package com.dladukedev.bibleyeartracker.bibleReading.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.readInformationDataStore: DataStore<Preferences> by preferencesDataStore(name = "read_information")

interface ReadInformationLocalDataSource {
    fun subscribeLastReadOffset(): Flow<Int?>
    suspend fun getLastReadOffset(): Int?
    suspend fun setLastReadOffset(lastReadOffset: Int?)
}

class ReadInformationLocalDataStoreSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : ReadInformationLocalDataSource {
    private val lastReadOffsetKey = intPreferencesKey("lastReadOffset")

    override fun subscribeLastReadOffset(): Flow<Int?> {
        return context.readInformationDataStore.data.map { data ->
            data[lastReadOffsetKey]
        }
    }

    override suspend fun getLastReadOffset(): Int? {
        return context.readInformationDataStore.data.first()[lastReadOffsetKey]
    }

    override suspend fun setLastReadOffset(lastReadOffset: Int?) {
        context.readInformationDataStore.edit { data ->
            when (lastReadOffset) {
                null -> data.remove(lastReadOffsetKey)
                else -> data[lastReadOffsetKey] = lastReadOffset
            }
        }
    }
}
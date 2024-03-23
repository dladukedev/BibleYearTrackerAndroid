package com.dladukedev.data.progress

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProgressOffsetRepository {
    val lastReadOffset: Flow<Int?>

    suspend fun incrementLastReadOffset()
    suspend fun decrementLastReadOffset()
    suspend fun clearLastReadOffset()
    suspend fun setMaxOffset(maxReadOffset: Int)
}

internal class ProgressOffsetRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): ProgressOffsetRepository {

    override val lastReadOffset = context.progressDataStore.data.map { data ->
        data[lastReadOffsetKey]
    }
    override suspend fun incrementLastReadOffset() {
        context.progressDataStore.edit { data ->
            val offset = data[lastReadOffsetKey] ?: -1
            val maxReadOffset = data.maxOffset

            // TODO: Better Exception
            if(offset == maxReadOffset) throw Exception("Attempted to increment to invalid offset from offset $offset")

            data[lastReadOffsetKey] = offset + 1
        }
    }

    override suspend fun decrementLastReadOffset() {
        context.progressDataStore.edit { data ->
            val offset = data[lastReadOffsetKey] ?: -1

            // TODO: Better Exception
            // TODO: Re set to null? Default to -1?
            if(offset < 0) throw Exception("Attempted to decrement to invalid offset from offset $offset")

            data[lastReadOffsetKey] = offset - 1
        }
    }

    override suspend fun clearLastReadOffset() {
        context.progressDataStore.edit { data ->
            data.remove(lastReadOffsetKey)
        }
    }

    override suspend fun setMaxOffset(maxReadOffset: Int) {
        context.progressDataStore.edit { data ->
            data[maxReadOffsetKey] = maxReadOffset
        }
    }

    private val lastReadOffsetKey = intPreferencesKey("lastReadOffset")
    private val maxReadOffsetKey = intPreferencesKey("maxReadOffsetKey")

    private val Preferences.maxOffset get(): Int =
        this[maxReadOffsetKey] ?: 365 // TODO: Set on app init?
}

package com.dladukedev.data.progress

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

internal val Context.progressDataStore: DataStore<Preferences> by preferencesDataStore(name = "progress")

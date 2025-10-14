package com.arijit4.nothing.notes.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

suspend fun Context.saveInt(key: String, value: Int) {
    this.dataStore.edit { settings ->
        settings[intPreferencesKey(key)] = value
    }
}

fun Context.readFlow(key: String): Flow<Int?> {
    return this.dataStore.data.map { preferences ->
        preferences[intPreferencesKey(key)]
    }
}

suspend fun Context.read(key: String): Int? {
    return this.readFlow(key).first()
}

enum class DataStoreKeys {
    WIDGET_SHAPE,

}
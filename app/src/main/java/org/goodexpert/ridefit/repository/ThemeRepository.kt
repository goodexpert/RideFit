package org.goodexpert.ridefit.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeRepository(private val context: Context) {

    private val keyDarkMode = booleanPreferencesKey("dark_mode")

    val isDarkMode: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[keyDarkMode] ?: false
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[keyDarkMode] = enabled
        }
    }
}

package com.muigorick.plashr.ui.activities.settings

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

const val PREFERENCE_NAME = "USER_PREFERENCES"

class DataStoreRepository @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(
            name = PREFERENCE_NAME
        )
    }

    private object PreferencesKeys {
        val PREF_APP_THEME = stringPreferencesKey(name = SettingsFragment.PREF_APP_THEME)
        val PREF_IMAGE_LAYOUT = stringPreferencesKey(name = SettingsFragment.PREF_IMAGE_LAYOUT)
        val PREF_LOAD_QUALITY = stringPreferencesKey(name = SettingsFragment.PREF_LOAD_QUALITY)
        val PREF_RATE_APP = stringPreferencesKey(name = SettingsFragment.PREF_RATE_APP)
        val PREF_FEEDBACK = stringPreferencesKey(name = SettingsFragment.PREF_FEEDBACK)
        val PREF_PRIVACY_POLICY = stringPreferencesKey(name = SettingsFragment.PREF_PRIVACY_POLICY)
        val PREF_ABOUT_US = stringPreferencesKey(name = SettingsFragment.PREF_ABOUT_US)
        val CLEAR_CACHE_SIZE = stringPreferencesKey(name = SettingsFragment.CLEAR_CACHE_SIZE)
    }


    private val dataStore = context.dataStore

    suspend fun saveThemeToDataStore(appTheme: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.PREF_APP_THEME] = appTheme
        }
    }

    val readThemeFromDataStore: Flow<String> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.d("DataStore", exception.message.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preference ->
        val appTheme = preference[PreferencesKeys.PREF_APP_THEME] ?: "Light"
        appTheme
    }

    val readImageLayoutFromDataStore: Flow<String> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.d("DataStore", exception.message.toString())
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preference ->

        preference[PreferencesKeys.PREF_IMAGE_LAYOUT] ?: "List"
       
    }

    suspend fun saveImageLayoutToDataStore(imageLayout: String) {
        dataStore.edit { preference ->
            preference[PreferencesKeys.PREF_IMAGE_LAYOUT] = imageLayout
        }
    }

}
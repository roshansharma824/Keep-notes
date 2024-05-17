package com.example.keepnotes.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class KeepNoteDataStore @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) {
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_PROFILE_URL = stringPreferencesKey("user_profile_url")
    }

    val getUserId: Flow<String> = dataStorePreferences.data.map { preferences ->
        preferences[USER_ID] ?: ""
    }

    val getUserProfileUrl: Flow<String> = dataStorePreferences.data.map { preferences ->
        preferences[USER_PROFILE_URL] ?: ""
    }

    suspend fun saveUserId(token: String) {
        dataStorePreferences.edit { preferences ->
            preferences[USER_ID] = token
        }
    }

    suspend fun saveUserProfileUrl(userProfileUrl: String) {
        dataStorePreferences.edit { preferences ->
            preferences[USER_PROFILE_URL] = userProfileUrl
        }
    }
}
package com.example.safeguard.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "safeguard_sessions")

class UserPreferences(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID = stringPreferencesKey("user_id")
    }

    val authToken: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun saveSession(token: String, userId: String) {
        context.dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
            pref[USER_ID] = userId
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}
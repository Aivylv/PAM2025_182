package com.example.safeguard.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey

val Context.dataStore by preferencesDataStore(name = "safeguard_sessions")

class UserPreferences(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID = stringPreferencesKey("user_id")
        private val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
    }

    //Mengambil Token untuk Header API
    val authToken: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    //Mengambil User ID untuk Audit Trail (Siapa yang mencatat barang)
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }

    //Cek apakah ini pertama kali aplikasi dijalankan (Default true)
    val isFirstRun: Flow<Boolean> = context.dataStore.data.map { it[IS_FIRST_RUN] ?: true }

    suspend fun saveSession(token: String, userId: String) {
        context.dataStore.edit { pref ->
            pref[TOKEN_KEY] = token
            pref[USER_ID] = userId
        }
    }

    suspend fun setFirstRunCompleted() {
        context.dataStore.edit { it[IS_FIRST_RUN] = false }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.remove(TOKEN_KEY)
            it.remove(USER_ID)
        }
    }
}
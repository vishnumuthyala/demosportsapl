package com.demosportsapl.app.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

data class SessionData(
    val role: String = "GENERAL",
    val tableId: Int = 25,
    val tableName: String = "Table 25"
)

class SessionDataStore(private val context: Context) {
    companion object {
        val KEY_ROLE = stringPreferencesKey("role")
        val KEY_TABLE_ID = intPreferencesKey("table_id")
        val KEY_TABLE_NAME = stringPreferencesKey("table_name")
    }

    val session: Flow<SessionData> = context.dataStore.data.map { prefs ->
        SessionData(
            role = prefs[KEY_ROLE] ?: "GENERAL",
            tableId = prefs[KEY_TABLE_ID] ?: 25,
            tableName = prefs[KEY_TABLE_NAME] ?: "Table 25"
        )
    }

    suspend fun saveSession(role: String, tableId: Int, tableName: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ROLE] = role
            prefs[KEY_TABLE_ID] = tableId
            prefs[KEY_TABLE_NAME] = tableName
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}

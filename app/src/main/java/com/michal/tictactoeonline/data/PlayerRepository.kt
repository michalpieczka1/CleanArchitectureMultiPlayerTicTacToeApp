package com.michal.tictactoeonline.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michal.tictactoeonline.data.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlayerRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val UID = stringPreferencesKey("uid")
        val SYMBOL = stringPreferencesKey("symbol")
        val WIN_COUNT = stringPreferencesKey("win_count")
    }


    val currentUsername: Flow<String> = dataStore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }
    val currentPassword: Flow<String> = dataStore.data.map { preferences ->
        preferences[PASSWORD] ?: ""
    }
    val currentUID: Flow<String> = dataStore.data.map { preferences ->
        preferences[UID] ?: ""
    }
    val currentSymbol: Flow<String> = dataStore.data.map { preferences ->
        preferences[SYMBOL] ?: ""
    }
    val currentWinCount: Flow<String> = dataStore.data.map { preferences ->
        preferences[WIN_COUNT] ?: "0"
    }

    val currentPlayer: Flow<Player> = combine(
        currentUsername,
        currentPassword,
        currentUID,
        currentSymbol,
        currentWinCount
    ) { username, password, uid, symbol, winCount ->
        Player(username, password, uid, symbol, winCount.toInt())
    }

    suspend fun doesPlayerExist(): Boolean {
        return dataStore.data.map { preferences ->
            preferences.contains(USERNAME)
                    && preferences.contains(PASSWORD)
                    && preferences.contains(UID)
                    && preferences.contains(SYMBOL)
        }.first()
    }

    suspend fun clearData() {
        dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME] = username
        }
    }

    suspend fun savePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[PASSWORD] = password
        }

    }

    suspend fun saveUID(uid: String) {
        dataStore.edit { preferences ->
            preferences[UID] = uid
        }
    }

    suspend fun saveSymbol(symbol: String) {
        dataStore.edit { preferences ->
            preferences[SYMBOL] = symbol
        }
    }

    suspend fun saveWinCount(winCount: Int) {
        dataStore.edit { preferences ->
            preferences[WIN_COUNT] = winCount.toString()
        }
    }


}
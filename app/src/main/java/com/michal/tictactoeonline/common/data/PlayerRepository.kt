package com.michal.tictactoeonline.common.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.michal.tictactoeonline.common.data.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlayerRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val UID = stringPreferencesKey("uid")
        val SYMBOL = stringPreferencesKey("symbol")
        val WIN_COUNT = intPreferencesKey("win_count")
        val IN_GAME = booleanPreferencesKey("in_game")
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
        preferences[SYMBOL] ?: "X"
    }
    val currentWinCount: Flow<Int> = dataStore.data.map { preferences ->
        preferences[WIN_COUNT] ?: 0
    }
    val currentInGame: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IN_GAME] ?: false
    }

    val currentPlayer: Flow<Player> = combine(
        currentUsername,
        currentPassword,
        currentUID,
        currentSymbol,
        currentWinCount,
        currentInGame
    ) { arrayOfValues: Array<Any?> ->
        val username = arrayOfValues[0] as String
        val password = arrayOfValues[1] as String
        val uid = arrayOfValues[2] as String
        val symbol = arrayOfValues[3] as String
        val winCount = arrayOfValues[4] as Int
        val inGame = arrayOfValues[5] as Boolean

        Player(
            username = username,
            password = password,
            uid = uid,
            symbol = symbol,
            winAmount = winCount,
            inGame = inGame
        )
    }


    suspend fun doesPlayerExist(): Boolean {
        return dataStore.data.map { preferences ->
            preferences.contains(USERNAME)
                    && preferences.contains(PASSWORD)
                    && preferences.contains(UID)
                    && preferences.contains(SYMBOL)
                    && preferences.contains(WIN_COUNT)
                    && preferences.contains(IN_GAME)
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
            preferences[WIN_COUNT] = winCount
        }
    }
    suspend fun saveInGame(inGame: Boolean) {
        dataStore.edit { preferences ->
            preferences[IN_GAME] = inGame
        }
        println("in game $inGame")
    }


}
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
import kotlinx.coroutines.flow.firstOrNull
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


    private fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>{
        return dataStore.data.map {
            it[key] ?: defaultValue
        }
    }

    val currentUsername: Flow<String> = getPreference(USERNAME, "")
    val currentPassword: Flow<String> = getPreference(PASSWORD,"")
    val currentUID: Flow<String> = getPreference(UID, "")
    val currentSymbol: Flow<String> = getPreference(SYMBOL, "X")
    val currentWinCount: Flow<Int> = getPreference(WIN_COUNT, 0)
    val currentInGame: Flow<Boolean> = getPreference(IN_GAME, false)

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
            onlineGamesBlocked = inGame
        )
    }


    suspend fun doesPlayerExist(): Boolean {
        return dataStore.data.map { preferences ->
            listOf(USERNAME, PASSWORD, UID).all { key ->
                preferences.contains(key) || preferences[key]?.isNotEmpty() ?: false
            }
        }.firstOrNull() ?: false
    }


    suspend fun clearData() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME)
            preferences.remove(PASSWORD)
            preferences.remove(UID)
            preferences.remove(SYMBOL)
            preferences.remove(WIN_COUNT)
            preferences.remove(IN_GAME)
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
    }


}
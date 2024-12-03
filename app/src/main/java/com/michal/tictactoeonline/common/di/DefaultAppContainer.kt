package com.michal.tictactoeonline.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.database.FirebaseDatabase
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.features.game.data.SessionsDBRepository
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository

class DefaultAppContainer(context: Context) : AppContainer {
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(
        name = "data"
    )

    override val sessionsDBRepository: SessionsDBRepository by lazy {
        SessionsDBRepository(databaseInstance)
    }
    override val playerRepository: PlayerRepository by lazy {
        PlayerRepository(dataStore = context.dataStore)
    }
    override val playersDBRepository: PlayersDBRepository by lazy{
        PlayersDBRepository(databaseInstance)
    }

}
package com.michal.tictactoeonline.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.database.FirebaseDatabase
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.PlayerRepository

class DefaultAppContainer(context: Context) : AppContainer {
    private val databaseInstance = FirebaseDatabase.getInstance()
    private val Context.dataStore:DataStore<Preferences> by preferencesDataStore(
        name = "data"
    )

    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(databaseInstance)
    }
    override val playerRepository: PlayerRepository by lazy {
        PlayerRepository(dataStore = context.dataStore)
    }
}
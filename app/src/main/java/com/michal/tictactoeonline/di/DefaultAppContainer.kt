package com.michal.tictactoeonline.di

import com.google.firebase.database.FirebaseDatabase
import com.michal.tictactoeonline.data.DatabaseRepository

class DefaultAppContainer : AppContainer {
    private val databaseInstance = FirebaseDatabase.getInstance()

    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepository(databaseInstance)
    }
}
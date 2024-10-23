package com.michal.tictactoeonline.di

import android.content.Context
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.PlayerRepository


interface AppContainer {
    val databaseRepository: DatabaseRepository
    val playerRepository: PlayerRepository
}
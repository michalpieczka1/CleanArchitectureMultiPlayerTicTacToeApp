package com.michal.tictactoeonline.di

import android.content.Context
import com.michal.tictactoeonline.data.DatabaseRepository


interface AppContainer {
    val databaseRepository: DatabaseRepository
}
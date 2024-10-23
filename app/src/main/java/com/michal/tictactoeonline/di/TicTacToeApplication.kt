package com.michal.tictactoeonline.di

import android.app.Application

class TicTacToeApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(applicationContext)
    }
}
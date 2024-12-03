package com.michal.tictactoeonline.common.di

import android.app.Application

class TicTacToeApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer(applicationContext)
    }
}
package com.michal.tictactoeonline.di

import android.content.Context
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.LocalTicTacToeRepositoryImpl


class AppContainer(context: Context) {

    val databaseRepository: DatabaseRepository by lazy{
        DatabaseRepository()
    }//TODO fix this
    private val localRepository: LocalTicTacToeRepositoryImpl = LocalTicTacToeRepositoryImpl()

    val localGameCointainer:LocalGameContainer? = null
}
package com.michal.tictactoeonline.di

import com.michal.tictactoeonline.data.TicTacToeRepository


class AppContainer() {

    val ticTacToeRepository: TicTacToeRepository by lazy{
        TicTacToeRepository()
    }
}
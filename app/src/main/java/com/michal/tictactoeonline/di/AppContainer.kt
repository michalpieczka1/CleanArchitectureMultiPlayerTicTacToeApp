package com.michal.tictactoeonline.di

import com.michal.tictactoeonline.TicTacToeRepository


class AppContainer() {

    val ticTacToeRepository: TicTacToeRepository by lazy{
        TicTacToeRepository()
    }
}
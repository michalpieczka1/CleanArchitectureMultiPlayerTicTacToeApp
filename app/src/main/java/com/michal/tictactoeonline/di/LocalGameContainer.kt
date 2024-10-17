package com.michal.tictactoeonline.di

import com.michal.tictactoeonline.data.LocalTicTacToeRepositoryImpl
import com.michal.tictactoeonline.presentation.vsPc.LocalGameViewModelFactory

class LocalGameContainer(
    localGameRepository: LocalTicTacToeRepositoryImpl
) {
    val localGameViewModelFactory = LocalGameViewModelFactory(localGameRepository)
}
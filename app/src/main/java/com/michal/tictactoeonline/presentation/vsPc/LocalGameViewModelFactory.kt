package com.michal.tictactoeonline.presentation.vsPc

import com.michal.tictactoeonline.data.LocalTicTacToeRepositoryImpl
import com.michal.tictactoeonline.di.Factory

class LocalGameViewModelFactory(
    private val localRepository: LocalTicTacToeRepositoryImpl
) : Factory<LocalGameViewModel> {
    override fun create(): LocalGameViewModel {
        return LocalGameViewModel(localRepository)
    }
}
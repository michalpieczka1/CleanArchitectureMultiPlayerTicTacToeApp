package com.michal.tictactoeonline.common.presentation.ticTacToeApp

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.features.game.presentation.main.MainScreenViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicTacToeViewModel(
    private val playerRepository:PlayerRepository
):ViewModel() {
    private val _isUserLogged = mutableStateOf<Boolean?>(null)  // null means still loading
    val isUserLogged: State<Boolean?> = _isUserLogged


    init {
        viewModelScope.launch {
                isPlayerAlreadyLogged()
        }
    }

    private suspend fun isPlayerAlreadyLogged(){
        val isLoggedIn = playerRepository.doesPlayerExist()
        _isUserLogged.value = isLoggedIn
    }
    companion object{
        fun provideFactory(): ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val playerRepository = application.appContainer.playerRepository
                    TicTacToeViewModel(playerRepository)
                }
            }
        }
    }
}
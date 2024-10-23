package com.michal.tictactoeonline.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.PlayerRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.TicTacToeApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val playerRepository: PlayerRepository
): ViewModel() {
    private val _playerState = MutableStateFlow<Player>(Player())
    val playerState:StateFlow<Player> = _playerState.asStateFlow()

    init {
        viewModelScope.launch {
            playerRepository.currentPlayer.collect{player ->
                _playerState.update {
                    player
                }
            }
        }
    }

    fun onLogOutClick(){
        viewModelScope.launch {
            playerRepository.clearData()
        }
    }

    companion object{
        fun provideFactory(): ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val repository = application.appContainer.playerRepository
                    MainScreenViewModel(repository)
                }
            }
        }
    }
}
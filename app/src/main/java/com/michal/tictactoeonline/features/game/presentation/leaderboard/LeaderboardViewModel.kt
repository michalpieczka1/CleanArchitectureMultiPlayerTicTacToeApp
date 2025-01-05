package com.michal.tictactoeonline.features.game.presentation.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.game.presentation.main.MainScreenViewModel
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val dbRepository: PlayersDBRepository
): ViewModel() {
    private val _state: MutableStateFlow<LeaderboardUiState> = MutableStateFlow(LeaderboardUiState())
    val state: StateFlow<LeaderboardUiState> = _state.asStateFlow()

    init{
        viewModelScope.launch {
            val list = dbRepository.getListOf100BestPlayers()
            _state.update {
                it.copy(
                    playerList = list,
                    dbResource = Resource.Loading()
                )
            }
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val playersDBRepository = application.appContainer.playersDBRepository
                    LeaderboardViewModel(dbRepository = playersDBRepository)
                }
            }
        }
    }
}
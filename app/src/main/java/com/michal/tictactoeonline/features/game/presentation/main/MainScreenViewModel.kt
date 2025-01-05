package com.michal.tictactoeonline.features.game.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val playerRepository: PlayerRepository,
    private val playersDBRepository: PlayersDBRepository
) : ViewModel() {
    private val _playerState = MutableStateFlow<Player>(Player())
    val playerState: StateFlow<Player> = _playerState.asStateFlow()

    init {
        viewModelScope.launch {
            playerRepository.currentPlayer.collect { localPlayer ->
                _playerState.update { state ->
                    state.copy(
                        username = localPlayer.username,
                        password = localPlayer.password,
                        uid = localPlayer.uid,
                        winAmount = localPlayer.winAmount,
                        inGame = localPlayer.inGame,
                        symbol = localPlayer.symbol,
                    )
                }
            }
            playerRepository.currentPlayer.collect { localPlayer ->
                playersDBRepository.observePlayer(localPlayer.uid).collect { snapshot ->
                    when (snapshot) {
                        is Resource.Error -> println("blad")
                        is Resource.Loading -> println("ladowanie")
                        is Resource.Success -> {
                            if (snapshot.data != null) {
                                val newPlayer = Player(
                                    username = snapshot.data.username,
                                    password = snapshot.data.password,
                                    uid = snapshot.data.uid,
                                    winAmount = snapshot.data.winAmount,
                                    inGame = snapshot.data.inGame,
                                    symbol = snapshot.data.symbol,
                                )
                                _playerState.update { state ->
                                    state.copy(
                                        username = newPlayer.username,
                                        password = newPlayer.password,
                                        uid = newPlayer.uid,
                                        winAmount = newPlayer.winAmount,
                                        inGame = newPlayer.inGame,
                                        symbol = newPlayer.symbol,
                                    )
                                }
                                playerRepository.saveUsername(newPlayer.username)
                                playerRepository.savePassword(newPlayer.password!!)
                                playerRepository.saveUID(newPlayer.uid)
                                playerRepository.saveInGame(newPlayer.inGame)
                                playerRepository.saveWinCount(newPlayer.winAmount)
                                playerRepository.saveSymbol(newPlayer.symbol!!)
                            }
                        }

                    }
                }
            }
        }
    }

    fun onLogOutClick(onLogOut: () -> Unit) {
        viewModelScope.launch {
            playerRepository.clearData()
            onLogOut()
        }
    }


    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val playerRepository = application.appContainer.playerRepository
                    val playersDBRepository = application.appContainer.playersDBRepository
                    MainScreenViewModel(playerRepository, playersDBRepository)
                }
            }
        }
    }
}
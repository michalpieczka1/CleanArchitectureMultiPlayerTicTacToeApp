package com.michal.tictactoeonline.features.game.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.data.ConnectivityRepository
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val playerRepository: PlayerRepository,
    private val playersDBRepository: PlayersDBRepository,
    private val connectivityRepository: ConnectivityRepository
) : ViewModel() {
    private val _playerState = MutableStateFlow<Player>(Player())
    private val isConnected = connectivityRepository.isConnected
    val playerState: StateFlow<Player> = _playerState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                updateOnlineBlockOnConnectionStatus()
            }

            playerRepository.currentPlayer
                .flatMapLatest { localPlayer ->
                playersDBRepository.observePlayer(localPlayer.uid)
                    .map { remoteData -> localPlayer to remoteData }
            }
                .collect { (localPlayer, remoteData) ->
                    Log.i("wartosc","local player: $localPlayer")
                    when (remoteData) {
                        is Resource.Error -> println("blad")
                        is Resource.Loading -> println("ladowanie")
                        is Resource.Success -> {
                            val remotePlayer = remoteData.data ?: return@collect
                            Log.i("wartosc","remote player: $remotePlayer")
                            _playerState.update { state ->
                                state.copy(
                                    username = remotePlayer.username,
                                    password = remotePlayer.password,
                                    uid = remotePlayer.uid,
                                    winAmount = remotePlayer.winAmount,
                                    onlineGamesBlocked = remotePlayer.onlineGamesBlocked,
                                    symbol = remotePlayer.symbol,
                                )
                            }
                            if(remotePlayer != localPlayer) {
                                    playerRepository.saveUsername(remotePlayer.username)
                                    playerRepository.savePassword(remotePlayer.password!!)
                                    playerRepository.saveUID(remotePlayer.uid)
                                    playerRepository.saveInGame(remotePlayer.onlineGamesBlocked)
                                    playerRepository.saveWinCount(remotePlayer.winAmount)
                                    playerRepository.saveSymbol(remotePlayer.symbol!!)
                                }
                            }
                        }
                }
        }
    }

    private suspend fun updateOnlineBlockOnConnectionStatus(){
        isConnected.collect{ isConnected ->
            _playerState.update {
                it.copy(
                    onlineGamesBlocked = !isConnected,
                    blockedGamesMessage = if(isConnected) Player.BlockedGamesMessages.PLAYER_PLAYING else Player.BlockedGamesMessages.NO_INTERNET_CONNECTION
                )
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
                    val connectivityRepository = application.appContainer.connectivityRepository
                    MainScreenViewModel(playerRepository, playersDBRepository, connectivityRepository)
                }
            }
        }
    }
}
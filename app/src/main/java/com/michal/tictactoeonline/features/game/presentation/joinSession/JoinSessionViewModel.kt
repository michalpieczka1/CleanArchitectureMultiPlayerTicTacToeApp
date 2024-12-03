package com.michal.tictactoeonline.features.game.presentation.joinSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.game.data.SessionsDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JoinSessionViewModel(
    private val sessionsDBRepository: SessionsDBRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinSessionUiState())
    val uiState: StateFlow<JoinSessionUiState> = _uiState.asStateFlow()

    init {
        setPlayer()
    }

    private fun setPlayer() {
        viewModelScope.launch {
            playerRepository.currentPlayer.collect { player ->
                _uiState.update {
                    it.copy(
                        player = player
                    )
                }
            }
        }
    }

    fun onSessionNameChange(newName: String) {
        _uiState.update {
            it.copy(sessionName = newName)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                sessionPassword = newPassword
            )
        }
    }

    fun onTryAgain(){
        _uiState.update {
            it.copy(
                resultResource = Resource.Loading(),
                sessionName = "",
                sessionPassword = ""
            )
        }
    }

    fun joinSessionClick(onJoinedKey: (sessionKey) -> Unit) {
        viewModelScope.launch {
            sessionsDBRepository.getSessionKeyByNameAndPassword(
                sessionName = uiState.value.sessionName,
                password = uiState.value.sessionPassword
            ).collect { session ->
                when (session) {
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                resultResource = Resource.Error(
                                    session.message ?: AppConstants.UNKNOWN_ERROR
                                )
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                resultResource = Resource.Loading()
                            )
                        }
                    }

                    is Resource.Success -> {
                        if (session.data == null) {
                            _uiState.update {
                                it.copy(
                                    resultResource = Resource.Error(AppConstants.UNKNOWN_ERROR)
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    resultResource = Resource.Success(session.data)
                                )
                            }
                            onJoinedKey(session.data)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = application.appContainer.sessionsDBRepository
                    val playerRepository = application.appContainer.playerRepository
                    JoinSessionViewModel(dbRepository, playerRepository)
                }
            }
        }
    }
}

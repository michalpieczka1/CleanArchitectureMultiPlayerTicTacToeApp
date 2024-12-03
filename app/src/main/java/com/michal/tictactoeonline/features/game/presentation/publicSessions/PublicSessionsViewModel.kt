package com.michal.tictactoeonline.features.game.presentation.publicSessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Session
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.game.data.SessionsDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PublicSessionsViewModel(
    private var sessionsDBRepository: SessionsDBRepository,
    private var playerRepository: PlayerRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<PublicSessionsUiState>(PublicSessionsUiState())
    val uiState: StateFlow<PublicSessionsUiState> = _uiState.asStateFlow()

    companion object {
        fun provideFactory(
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = application.appContainer.sessionsDBRepository
                    val playerRepository = application.appContainer.playerRepository
                    PublicSessionsViewModel(
                        sessionsDBRepository = dbRepository,
                        playerRepository = playerRepository
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            setPlayer()
            observeAllSessions()
        }
    }

    private suspend fun setPlayer() {
        playerRepository.currentPlayer.first { player ->
            _uiState.update {
                it.copy(
                    player = player
                )
            }
            true
        }
    }

    fun removeSession(session: Session) {
        _uiState.update {
            it.copy(
                sessions = it.sessions.filter { element -> element != session }
            )
        }
    }

    private suspend fun observeAllSessions() {
        sessionsDBRepository.observeAllSessions().collect { sessionsResource ->
            when (sessionsResource) {
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            sessions = emptyList(),
                            sessionResource = Resource.Error(
                                sessionsResource.message ?: AppConstants.UNKNOWN_ERROR
                            )
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            sessionResource = Resource.Loading()
                        )
                    }
                }

                is Resource.Success -> {
                    val sessions = sessionsResource.data

                    _uiState.update {
                        it.copy(
                            sessions = sessions?.filter { element -> element.playerCount < 2 && element.sessionPassword.isBlank() && element.win == null }
                                ?: emptyList(),
                            sessionResource = Resource.Success(true)
                        )
                    }
                }
            }
        }
    }



    fun onSessionJoin(
        navigationJoinMethod: (sessionKey) -> Unit,
        sessionName: String,
        sessionPassword: String
    ) {
        viewModelScope.launch {
            sessionsDBRepository.getSessionKeyByNameAndPassword(sessionName, sessionPassword)
                .collect { sessionResource ->
                    when (sessionResource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    sessionResource = Resource.Error(
                                        sessionResource.message ?: AppConstants.UNKNOWN_ERROR
                                    )
                                )
                            }
                        }

                        is Resource.Loading -> {
                            _uiState.update {
                                it.copy(
                                    sessionResource = Resource.Loading()
                                )
                            }
                        }

                        is Resource.Success -> {
                            if (sessionResource.data == null) {
                                _uiState.update {
                                    it.copy(
                                        sessionResource = Resource.Error("Error joining the session")
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        sessionResource = Resource.Success(true)
                                    )
                                }
                                navigationJoinMethod(sessionResource.data)
                            }
                        }
                    }
                }
        }
    }
}
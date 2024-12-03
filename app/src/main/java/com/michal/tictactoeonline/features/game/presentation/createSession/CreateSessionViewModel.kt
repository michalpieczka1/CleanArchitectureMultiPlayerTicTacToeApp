package com.michal.tictactoeonline.features.game.presentation.createSession

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

class CreateSessionViewModel(
    private val sessionsDBRepository: SessionsDBRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState: StateFlow<CreateSessionUiState> = _uiState.asStateFlow()

    lateinit var sessionKey: String



    fun onSessionNameChange(newName: String) {
        _uiState.update {
            it.copy(
                sessionName = newName
            )
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

    fun createSessionClick(onSessionCreate: (String) -> Unit) {
        if(uiState.value.sessionName != ""){
            viewModelScope.launch {
                sessionsDBRepository.createSession(
                    uiState.value.sessionName,
                    uiState.value.sessionPassword
                ).collect { createSessionResource ->
                    when (createSessionResource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    resultResource = Resource.Error(
                                        createSessionResource.message ?: AppConstants.UNKNOWN_ERROR
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
                            if (createSessionResource.data != null) {
                                val newSessionKey = createSessionResource.data
                                sessionKey = newSessionKey
                                _uiState.update {
                                    it.copy(
                                        resultResource = Resource.Success(true)
                                    )
                                }
                                onSessionCreate(sessionKey)
                            } else {
                                _uiState.update {
                                    it.copy(
                                        resultResource = Resource.Error("Failed creating session.")
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }else{
            _uiState.update {
                it.copy(
                    resultResource = Resource.Error("Session name cannot be empty.")// TODO in future two different errors, if its hard error do dialog if not then error in ui.
                )
            }
        }
    }



    companion object {
        fun provideFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.sessionsDBRepository
                    val playerRepository = applicaton.appContainer.playerRepository
                    CreateSessionViewModel(dbRepository, playerRepository)
                }
            }
        }
    }
}
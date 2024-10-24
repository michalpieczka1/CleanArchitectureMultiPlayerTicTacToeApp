package com.michal.tictactoeonline.presentation.createSession

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.PlayerRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateSessionViewModel(
    private val databaseRepository: DatabaseRepository,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState: StateFlow<CreateSessionUiState> = _uiState.asStateFlow()

    lateinit var sessionKey: String


    init {
        setPlayer()
    }

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
                password = newPassword
            )
        }
    }

    private fun setPlayer() {
        viewModelScope.launch {
            playerRepository.currentPlayer.collect { player ->
                _uiState.update {
                    it.copy(
                        player = player
                    )
                }
                Log.i("test", uiState.value.toString())
            }
        }
    }

    fun createSessionClick(onSessionCreate: (String) -> Unit) {
        viewModelScope.launch {
            databaseRepository.createSession(
                uiState.value.player,
                uiState.value.sessionName,
                uiState.value.password
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
                                    println(uiState.value)
                            onSessionCreate(sessionKey)
                        } else {
                            _uiState.update {
                                it.copy(
                                    resultResource = Resource.Error("Session not found")
                                )
                            }
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
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.databaseRepository
                    val playerRepository = applicaton.appContainer.playerRepository
                    CreateSessionViewModel(dbRepository, playerRepository)
                }
            }
        }
    }
}
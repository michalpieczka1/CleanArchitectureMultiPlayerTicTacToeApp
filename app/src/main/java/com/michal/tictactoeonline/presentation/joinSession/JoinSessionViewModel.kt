package com.michal.tictactoeonline.presentation.joinSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JoinSessionViewModel(
    private val databaseRepository: DatabaseRepository,
    val player: Player
) : ViewModel() {
    private val _uiState = MutableStateFlow(JoinSessionUiState())
    val uiState: StateFlow<JoinSessionUiState> = _uiState.asStateFlow()

    var sessionKey: String? = null

    fun onSessionNameChange(newName: String) {
        _uiState.update {
            it.copy(sessionName = newName)
        }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update {
            it.copy(
                password = newPassword
            )
        }
    }

    fun joinSessionClick() {

        viewModelScope.launch {
            val foundSessionKey = databaseRepository.getSessionKeyByNameAndPassword(
                sessionName = uiState.value.sessionName,
                password = uiState.value.password
            ).first()
            when (foundSessionKey) {
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = foundSessionKey.message
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }
                }

                is Resource.Success -> {
                    if(foundSessionKey.data != null){
                        sessionKey = foundSessionKey.data
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Session not found"
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            player: Player
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.databaseRepository
                    JoinSessionViewModel(dbRepository, player)
                }
            }
        }
    }
}

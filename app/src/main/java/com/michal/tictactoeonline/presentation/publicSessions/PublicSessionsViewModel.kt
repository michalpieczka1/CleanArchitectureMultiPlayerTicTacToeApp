package com.michal.tictactoeonline.presentation.publicSessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PublicSessionsViewModel(
    private var databaseRepository: DatabaseRepository,
    val player: Player
) :ViewModel() {
    private val _uiState = MutableStateFlow<PublicSessionsUiState>(PublicSessionsUiState.Loading)
    val uiState:StateFlow<PublicSessionsUiState> = _uiState.asStateFlow()

    companion object {
        fun provideFactory(
            player: Player
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = application.appContainer.databaseRepository
                    PublicSessionsViewModel(databaseRepository = dbRepository, player = player)
                }
            }
        }
    }

    init {
        getSessions()
    }

    fun removeSession(session: Session){
        val currentState = _uiState.value
        if(currentState is PublicSessionsUiState.Success){
            val updatedSessions = currentState.sessions?.filter { it != session }
            _uiState.value = PublicSessionsUiState.Success(updatedSessions)
        }
    }

    private fun getSessions(){
        viewModelScope.launch {
            databaseRepository.getAllSessions().collect{ sessionsResource ->
                when(sessionsResource){
                    is Resource.Error -> {
                        _uiState.value = PublicSessionsUiState.Error(sessionsResource.message ?: AppConstants.UNKNOWN_ERROR)
                    }

                    is Resource.Loading -> {
                        _uiState.value = PublicSessionsUiState.Loading
                    }

                    is Resource.Success -> {
                        _uiState.value = PublicSessionsUiState.Success(sessionsResource.data)
                    }
                }
            }
        }
    }

}
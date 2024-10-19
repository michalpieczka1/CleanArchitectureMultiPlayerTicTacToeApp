package com.michal.tictactoeonline.presentation.createSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateSessionViewModel(
    private val databaseRepository: DatabaseRepository,
    val player:Player
): ViewModel() {
    private val _uiState = MutableStateFlow(CreateSessionUiState())
    val uiState:StateFlow<CreateSessionUiState> = _uiState.asStateFlow()

    lateinit var sessionKey:Resource<Any>


    fun onSessionNameChange(newName:String){
        _uiState.update {
            it.copy(
                sessionName = newName
            )
        }
    }

    fun onPasswordChange(newPassword:String){
            _uiState.update {
                it.copy(
                    password = newPassword
                )
            }
    }

    fun createSessionClick(){
        viewModelScope.launch {
            databaseRepository.createSession(player,uiState.value.sessionName,uiState.value.password).collect{
                when(it){
                    is Resource.Error -> {
                        sessionKey = Resource.Error(it.message ?: AppConstants.UNKNOWN_ERROR)
                    }
                    is Resource.Loading -> {
                        sessionKey = Resource.Loading()
                    }
                    is Resource.Success -> {
                        if (it.data != null){
                            val newSessionKey = it.data.keys.first()
                            sessionKey = Resource.Success(
                                newSessionKey
                            )
                        }else{
                            sessionKey = Resource.Error("Session not found")
                        }
                    }
                }
            }

        }
    }

    companion object{
        fun provideFactory(
            player: Player
        ):ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.databaseRepository
                    CreateSessionViewModel(dbRepository,player)
                }
            }
        }
    }
}
package com.michal.tictactoeonline.presentation.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.PlayerRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.TicTacToeApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val playerRepository: PlayerRepository
): ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    init {
        isPlayerAlreadyLogged()
    }

    fun onUsernameChange(username:String){
        _state.update {
            it.copy(username = username)
        }
    }

    fun onPasswordChange(password:String){
        _state.update {
            it.copy(password = password)
        }
    }

    fun onRegisterClick(){
        val isUsernameValid = state.value.username.isNotBlank()
        if(!isUsernameValid){
            _state.update {
                it.copy(usernameError = AppConstants.EMPTY_USERNAME)
            }
        }else{
            val uid = java.util.UUID.randomUUID().toString()

            viewModelScope.launch {
                playerRepository.saveUsername(state.value.username)
                playerRepository.savePassword(state.value.password)
                playerRepository.saveUID(uid)
                playerRepository.saveSymbol("X")
            }
        }
    }

    private fun isPlayerAlreadyLogged(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isUserAlreadyLogged = ( playerRepository.doesPlayerExist())
                )
            }
        }
    }

    companion object{
        fun provideFactory() : ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val repository = application.appContainer.playerRepository
                    RegisterViewModel(repository)
                }
        }}
    }
}
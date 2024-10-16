package com.michal.tictactoeonline.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel: ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    lateinit var player: Player

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

            player = Player(
                username = state.value.username,
                password = state.value.password,
                uid = uid
            )
        }
    }
}
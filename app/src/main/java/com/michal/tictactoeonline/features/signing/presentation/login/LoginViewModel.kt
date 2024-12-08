package com.michal.tictactoeonline.features.signing.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val playerRepository: PlayerRepository,
    private val playersDBRepository: PlayersDBRepository
): ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init {
        isPlayerAlreadyLogged()
    }

    fun onUsernameChange(username:String){
        _state.update {
            it.copy(
                username = username,
                isButtonClicked = false
            )
        }
    }

    fun onPasswordChange(password:String){
        _state.update {
            it.copy(password = password)
        }
    }

    fun onLoginClick(nextScreenNavigate:() -> Unit){
        _state.update {
            it.copy(
                isButtonClicked = true
            )
        }

        val isUsernameValid = state.value.username.isNotBlank()

        if(!isUsernameValid){
            _state.update {
                it.copy(
                    usernameError = AppConstants.EMPTY_USERNAME,
                    dbResource = null
                )
            }
        }else{
            _state.update {
                it.copy(usernameError = null)
            }

            viewModelScope.launch {
                when(val resource = playersDBRepository.getPlayer(state.value.username, state.value.password)){
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                dbResource = Resource.Error(resource.message!!)
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                dbResource = Resource.Loading()
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                dbResource = Resource.Success(true)
                            )
                        }
                        playerRepository.saveUsername(state.value.username)
                        playerRepository.savePassword(state.value.password)
                        playerRepository.saveUID(resource.data?.uid!!)
                        playerRepository.saveSymbol(resource.data.symbol!!)
                        playerRepository.saveInGame(resource.data.inGame)
                        playerRepository.saveWinCount(resource.data.winAmount)
                        nextScreenNavigate()
                    }
                }
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
                    val dbRepository = application.appContainer.playersDBRepository
                    LoginViewModel(repository, dbRepository)
                }
        }}
    }
}
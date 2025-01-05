package com.michal.tictactoeonline.features.signing.presentation.register

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val playerRepository: PlayerRepository,
    private val playersDBRepository: PlayersDBRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RegisterUiState())
    val state: StateFlow<RegisterUiState> = _state.asStateFlow()

    private var warningDialogAlreadyShown: Boolean = false
    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username,
                isButtonClicked = false,
                dbResource = null
            )
        }
        warningDialogAlreadyShown = false
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                isButtonClicked = false,
                dbResource = null
            )
        }
    }

    fun hideWarningDialog() {
        _state.update {
            it.copy(
                showWarningDialog = false
            )
        }
    }

    fun onRegisterClick(nextScreenNavigate: () -> Unit) {
        _state.update {
            it.copy(
                isButtonClicked = true
            )
        }
        val isUsernameValid = state.value.username.isNotBlank()
        if (!isUsernameValid) {
            _state.update {
                it.copy(
                    usernameError = AppConstants.ERROR_EMPTY_USERNAME,
                    dbResource = null
                )
            }
        } else {
            _state.update {
                it.copy(usernameError = null)
            }

            if (state.value.dbResource !is Resource.Error && state.value.usernameError == null) {
                val player =
                    Player(username = state.value.username, password = state.value.password)

                viewModelScope.launch {
                    if(playersDBRepository.getPlayer(player.username,player.password) is Resource.Success){
                        _state.update {
                            it.copy(
                                dbResource = Resource.Error(AppConstants.ERROR_USERNAME_EXISTS)
                            )
                        }
                        return@launch
                    }else if (state.value.password == "" && !warningDialogAlreadyShown) {
                        _state.update {
                            it.copy(
                                showWarningDialog = true
                            )
                        }
                        warningDialogAlreadyShown = true
                        return@launch
                    } else {
                        playersDBRepository.createPlayer(player = player)
                            .collect { createPlayerResource ->
                                when (createPlayerResource) {
                                    is Resource.Error -> {
                                        _state.update {
                                            it.copy(
                                                dbResource = Resource.Error(createPlayerResource.message!!)
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
                                        playerRepository.saveUID(createPlayerResource.data!!)
                                        playerRepository.saveSymbol("X")
                                        playerRepository.saveInGame(false)
                                        playerRepository.saveWinCount(0)
                                        nextScreenNavigate()

                                    }
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
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val repository = application.appContainer.playerRepository
                    val dbRepository = application.appContainer.playersDBRepository
                    RegisterViewModel(repository, dbRepository)
                }
            }
        }
    }
}
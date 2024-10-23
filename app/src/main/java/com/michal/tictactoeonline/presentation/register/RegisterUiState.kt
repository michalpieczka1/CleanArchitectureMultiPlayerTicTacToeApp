package com.michal.tictactoeonline.presentation.register

import com.michal.tictactoeonline.data.model.Player

data class RegisterUiState(
    val username:String = "",
    val usernameError: String? = null,
    val password: String = "",
    val isUserAlreadyLogged:Boolean = false,
)

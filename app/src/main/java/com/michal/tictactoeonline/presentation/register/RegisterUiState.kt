package com.michal.tictactoeonline.presentation.register

data class RegisterUiState(
    val username:String = "",
    val usernameError: String? = null,
    val password: String = "",
    val isUserAlreadyLogged:Boolean = false,
    val isButtonClicked:Boolean = false
)

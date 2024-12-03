package com.michal.tictactoeonline.features.signing.presentation.register

import com.michal.tictactoeonline.common.util.Resource

data class RegisterUiState(
    val username:String = "",
    val usernameError: String? = null,
    val password: String = "",
    val isUserAlreadyLogged:Boolean = false,
    val isButtonClicked:Boolean = false,
    val dbResource: Resource<Any>? = null
)

package com.michal.tictactoeonline.features.signing.presentation.login

import com.michal.tictactoeonline.common.util.Resource

data class LoginUiState(
    val username:String = "",
    val usernameError: String? = null,
    val password: String = "",
    val isUserAlreadyLogged:Boolean = false,
    val isButtonClicked:Boolean = false,
    val dbResource: Resource<Any>? = null
)

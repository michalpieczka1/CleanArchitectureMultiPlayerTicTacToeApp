package com.michal.tictactoeonline.presentation.createSession

import com.michal.tictactoeonline.util.Resource

data class CreateSessionUiState(
    val sessionName:String = "",
    val sessionPassword:String = "",
    val resultResource:Resource<Any> = Resource.Loading()
)

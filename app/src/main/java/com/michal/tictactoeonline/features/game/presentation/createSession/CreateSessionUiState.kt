package com.michal.tictactoeonline.features.game.presentation.createSession

import com.michal.tictactoeonline.common.util.Resource

data class CreateSessionUiState(
    val sessionName:String = "",
    val sessionPassword:String = "",
    val resultResource: Resource<Any> = Resource.Loading()
)

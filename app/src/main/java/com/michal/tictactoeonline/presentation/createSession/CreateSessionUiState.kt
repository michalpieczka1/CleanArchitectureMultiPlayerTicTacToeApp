package com.michal.tictactoeonline.presentation.createSession

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.util.Resource

data class CreateSessionUiState(
    val sessionName:String = "",
    val password:String = "",
    val player: Player = Player(),
    val resultResource:Resource<Any> = Resource.Loading()
)

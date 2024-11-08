package com.michal.tictactoeonline.presentation.joinSession

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.util.Resource

data class JoinSessionUiState(
    val sessionName: String = "",
    val sessionPassword: String = "",
    val player: Player = Player(),
    val resultResource: Resource<String> = Resource.Loading()
)
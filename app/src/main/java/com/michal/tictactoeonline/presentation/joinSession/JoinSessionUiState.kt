package com.michal.tictactoeonline.presentation.joinSession

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.util.Resource

data class JoinSessionUiState(
    val sessionName: String = "",
    val password: String = "",
    val player: Player = Player(),
    val sessionResource: Resource<String> = Resource.Loading()
)
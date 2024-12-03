package com.michal.tictactoeonline.features.game.presentation.joinSession

import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.util.Resource

data class JoinSessionUiState(
    val sessionName: String = "",
    val sessionPassword: String = "",
    val player: Player = Player(),
    val resultResource: Resource<String> = Resource.Loading()
)
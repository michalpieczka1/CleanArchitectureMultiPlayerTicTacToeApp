package com.michal.tictactoeonline.presentation.onlineGame

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource

data class OnlineGameUiState(
    val player1: Player = Player(),
    val player2: Player? = null,
    val session: Session = Session(),
    val sessionResource: Resource<Any> = Resource.Loading()
)

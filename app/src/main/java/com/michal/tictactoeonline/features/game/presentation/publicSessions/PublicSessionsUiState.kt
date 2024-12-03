package com.michal.tictactoeonline.features.game.presentation.publicSessions

import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.data.model.Session
import com.michal.tictactoeonline.common.util.Resource

data class PublicSessionsUiState(
    val player: Player = Player(),
    val sessions: List<Session> = emptyList(),
    val sessionResource: Resource<Any?> = Resource.Loading()
)

package com.michal.tictactoeonline.presentation.publicSessions

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource

data class PublicSessionsUiState(
    val player:Player = Player(),
    val sessions: List<Session> = emptyList(),
    val sessionResource:Resource<Any?> = Resource.Loading()
)

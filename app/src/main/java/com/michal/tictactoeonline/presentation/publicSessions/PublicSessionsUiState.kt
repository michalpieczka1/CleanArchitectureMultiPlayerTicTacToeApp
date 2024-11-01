package com.michal.tictactoeonline.presentation.publicSessions

import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.Flow

data class PublicSessionsUiState(
    val player:Player = Player(),
    val sessions: List<Session>? = null,
    val sessionResource:Resource<Any?> = Resource.Loading()
)

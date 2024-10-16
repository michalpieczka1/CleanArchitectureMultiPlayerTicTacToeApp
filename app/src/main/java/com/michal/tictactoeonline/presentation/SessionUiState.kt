package com.michal.tictactoeonline.presentation

import com.michal.tictactoeonline.data.model.Session

data class SessionUiState(
    val session: Session? = null,
    val sessionKey: String? = null,
    val username: String = "", //TODO should be in room
)

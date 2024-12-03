package com.michal.tictactoeonline.features.game.presentation.onlineGame

import com.michal.tictactoeonline.common.data.model.Session
import com.michal.tictactoeonline.common.util.Resource

data class OnlineGameUiState(
    val session: Session = Session(),
    val sessionResource: Resource<Any> = Resource.Loading()
)

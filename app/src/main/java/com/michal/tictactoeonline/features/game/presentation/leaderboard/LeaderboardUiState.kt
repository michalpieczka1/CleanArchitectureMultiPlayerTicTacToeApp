package com.michal.tictactoeonline.features.game.presentation.leaderboard

import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.util.Resource

data class LeaderboardUiState(
    val dbResource: Resource<Any?> = Resource.Loading(),
    val playerList: List<Player> = listOf()
)

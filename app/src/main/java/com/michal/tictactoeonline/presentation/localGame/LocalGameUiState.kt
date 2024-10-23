package com.michal.tictactoeonline.presentation.localGame

import com.michal.tictactoeonline.data.model.Player

data class LocalGameUiState(
    val player: Player = Player(),
    val playerPC: Player = Player("PC", symbol = "O"),
    val currentTurn: Player = player,
    val board: List<String> = List(9) {""},
    val winner: Player? = null,
    val isWin: Boolean? = null,
    val isTie: Boolean? = null
)

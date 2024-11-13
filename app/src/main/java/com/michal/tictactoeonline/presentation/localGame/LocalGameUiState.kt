package com.michal.tictactoeonline.presentation.localGame

import com.michal.tictactoeonline.data.model.Player

data class LocalGameUiState(
    val player1: Player = Player(),
    val player2: Player = Player("Player2", symbol = "O"),//TODO make a window to change this to other name
    val currentTurn: Player = player1,
    val board: List<String> = List(9) {""},
    val winner: Player? = null,
    val isWin: Boolean? = null,
    val isTie: Boolean? = null
)

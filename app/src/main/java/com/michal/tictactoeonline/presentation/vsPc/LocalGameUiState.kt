package com.michal.tictactoeonline.presentation.vsPc

import com.michal.tictactoeonline.data.model.Player

data class LocalGameUiState(
    val player: Player = Player(),
    val playerPC: Player = Player("PC"),
    val currentTurn: Player = player,
    val board: List<String> = List(9) {""},
    val winnerSymbol: String? = null,
    val isTie: Boolean = false
)

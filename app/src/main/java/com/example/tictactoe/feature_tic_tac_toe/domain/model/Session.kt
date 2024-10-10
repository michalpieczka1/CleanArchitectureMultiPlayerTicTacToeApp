package com.example.tictactoe.feature_tic_tac_toe.domain.model

data class Session(
    val id:Long = 0,
    val player1:Player = Player(),
    val player2: Player? = null,
    val playerCount:Int = 1,
    val isWin:Boolean = false,
    val currentTurn: String,
    val board: List<Int?> = List(9) {null},
    val createdAt: Long = 0

)

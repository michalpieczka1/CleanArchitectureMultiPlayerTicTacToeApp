package com.michal.tictactoeonline.data

import com.michal.tictactoeonline.data.model.Player
import kotlinx.coroutines.flow.Flow

interface TicTacToeRepository {
    fun updateBoard(board:List<String>, player: Player,indexClicked: Int): List<String>
    fun checkWinnerSymbol(board:List<String>) : String?
    fun isTie(board: List<String>) : Boolean
}
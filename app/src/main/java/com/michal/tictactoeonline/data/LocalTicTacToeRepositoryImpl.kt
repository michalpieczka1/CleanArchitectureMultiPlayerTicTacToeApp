package com.michal.tictactoeonline.data

import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.util.Resource

class LocalTicTacToeRepositoryImpl : TicTacToeRepository {
    override fun updateBoard(board: List<String>, player: Player, indexClicked: Int) : List<String> {
        val tempBard = board.toMutableList()
        tempBard[indexClicked] = player.symbol ?: ""
        return tempBard
    }


    override fun checkWinnerSymbol(board: List<String>): String? {
        val calculatedRows = calculateRows(board)
        val calculatedColumns = calculateColumns(board)
        val calculatedAxis = calculateAxis(board)

        calculatedColumns?.let { return it }
        calculatedRows?.let { return it }
        calculatedAxis?.let { return it }

        return null

    }
    private fun calculateRows(board:List<String>): String?{
        for (i in 0 until 9 step 3){
            if(board[i] == board[i+1] && board[i] == board[i+2]){
                return board[i]
            }
        }
        return null
    }
    private fun calculateColumns(board:List<String>): String?{
        for (i in 0 until 9 step 3){
            if(board[i] == board[i+3] && board[i] == board[i+6]){
                return board[i]
            }
        }
        return null
    }
    private fun calculateAxis(board: List<String>): String?{
        if(board[0] == board[4] && board[0] == board[8]){
            return board[0]
        }else if(board[2] == board[4] && board[2] == board[6]){
            return board[2]
        }else{
            return null
        }
    }

    override fun isTie(board: List<String>): Boolean {
        return checkWinnerSymbol(board) == null
    }

}
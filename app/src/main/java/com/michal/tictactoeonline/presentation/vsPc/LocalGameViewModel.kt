package com.michal.tictactoeonline.presentation.vsPc

import androidx.lifecycle.ViewModel
import com.michal.tictactoeonline.data.LocalTicTacToeRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalGameViewModel(
    private val localTicTacToeRepositoryImpl: LocalTicTacToeRepositoryImpl
): ViewModel() {
    private val _state = MutableStateFlow(LocalGameUiState())
    val state: StateFlow<LocalGameUiState> = _state.asStateFlow()

    fun onBoardClicked(index: Int){
        if(state.value.board[index] == ""){
            val oldBoard = state.value.board
            val newBoard = oldBoard.toMutableList()
            newBoard[index] = state.value.currentTurn.symbol ?: ""

            val currentPlayer = if(state.value.currentTurn == state.value.player) state.value.playerPC else state.value.player
            _state.update {
                it.copy(
                    board = newBoard,
                    currentTurn = currentPlayer,
                    winnerSymbol = localTicTacToeRepositoryImpl.checkWinnerSymbol(board = state.value.board),
                    isTie = localTicTacToeRepositoryImpl.isTie(board = state.value.board)
                )
            }
        }
    }


}
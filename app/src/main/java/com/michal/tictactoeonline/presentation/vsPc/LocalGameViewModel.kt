package com.michal.tictactoeonline.presentation.vsPc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocalGameViewModel(
    private val player: Player
) : ViewModel() {
    private val _uiState = MutableStateFlow(LocalGameUiState())
    val uiState: StateFlow<LocalGameUiState> = _uiState.asStateFlow()

    companion object {
        fun provideFactory(
            player: Player
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    LocalGameViewModel(player = player)
                }
            }
        }
    }
    //TODO add savedStateHandle
    init {
        _uiState.update {
            it.copy(
                player = player,
                currentTurn = player,
                board = List(9) { "" },
                winner = null,
                isWin = null,
                isTie = null
            )
        }
    }

    fun onBoardClicked(indexClicked: Int) {
        if (uiState.value.board[indexClicked] == "" && uiState.value.isWin == null && uiState.value.isTie == null) {
            val oldBoard = uiState.value.board
            val newBoard = oldBoard.toMutableList()
            newBoard[indexClicked] = uiState.value.currentTurn.symbol ?: ""

            _uiState.update {
                it.copy(
                    board = newBoard
                )
            }

            if (isWin(indexClicked)) {
                _uiState.update {
                    it.copy(
                        winner = uiState.value.currentTurn,
                        isWin = isWin(indexClicked),
                        isTie = false,
                    )
                }
                return
            }
            else if(isBoardFilled()){
                _uiState.update {
                    it.copy(
                        winner = null,
                        isWin = false,
                        isTie = true
                    )
                }
                return
            }
            val newCurrentPlayer = if (uiState.value.currentTurn == uiState.value.player) uiState.value.playerPC else uiState.value.player
            _uiState.update {
                it.copy(
                    currentTurn = newCurrentPlayer
                )
            }
        }
    }
    private fun isWin(indexClicked: Int): Boolean {
        val board = uiState.value.board
        if (board.all { it == "" }) return false

        val isColOrRowWin = when (indexClicked) {
            0, 1, 2 -> {
                (board[0] == board[1] && board[1] == board[2])
                        ||
                        (board[indexClicked] == board[indexClicked + 3] && board[indexClicked + 3] == board[indexClicked + 6])
            }

            3, 4, 5 -> {
                (board[3] == board[4] && board[4] == board[5])
                        ||
                        (board[indexClicked] == board[indexClicked - 3] && board[indexClicked - 3] == board[indexClicked + 3])
            }

            6, 7, 8 -> {
                (board[6] == board[7] && board[7] == board[8])
                        ||
                        (board[indexClicked] == board[indexClicked - 3] && board[indexClicked - 3] == board[indexClicked - 6])
            }

            else -> false
        }

        val isAxisWin = when (indexClicked) {
            0, 8 -> {
                (board[0] == board[4] && board[4] == board[8])
            }

            2, 6 -> {
                (board[2] == board[4] && board[4] == board[6])
            }

            4 -> {
                (board[0] == board[4] && board[4] == board[8]) || (board[2] == board[4] && board[4] == board[6])
            }

            else -> false
        }
        return (isColOrRowWin || isAxisWin)
    }

    private fun isBoardFilled(): Boolean {
        return uiState.value.board.all { it != "" }
    }
}
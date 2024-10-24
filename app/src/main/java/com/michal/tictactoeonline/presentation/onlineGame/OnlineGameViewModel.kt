package com.michal.tictactoeonline.presentation.onlineGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.PlayerRepository
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnlineGameViewModel(
    private val databaseRepository: DatabaseRepository,
    private val playerRepository: PlayerRepository,
    private val sessionKey:String
): ViewModel() {
    private val _sessionUiState = MutableStateFlow(OnlineGameUiState())
    val sessionUiState: StateFlow<OnlineGameUiState> = _sessionUiState.asStateFlow()

    companion object{
        fun provideFactory(sessionKey: String):ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.databaseRepository
                    val playerRepository = applicaton.appContainer.playerRepository
                    OnlineGameViewModel(databaseRepository = dbRepository, playerRepository = playerRepository, sessionKey =  sessionKey)
                }
            }
        }
    }

    init {
        getPlayer1()
    }

    private fun getPlayer1(){
        viewModelScope.launch {
            playerRepository.currentPlayer.collect{player ->
                _sessionUiState.update {
                    it.copy(
                        player1 = player
                    )
                }
            }
        }
    }

    fun updateBoard(indexClicked: Int){
        if (sessionUiState.value.session.board[indexClicked] == "" && sessionUiState.value.session.isWin == null && sessionUiState.value.session.isTie == null) {
            val oldBoard = sessionUiState.value.session.board
            val newBoard = oldBoard.toMutableList()
            newBoard[indexClicked] = sessionUiState.value.session.currentTurn.symbol ?: ""

            _sessionUiState.update {
                it.copy(
                    session = sessionUiState.value.session.copy(board = newBoard)
                )
            }

            if (isWin(indexClicked)) {
                _sessionUiState.update {
                    it.copy(
                        session = sessionUiState.value.session.copy(
                            winner = sessionUiState.value.session.currentTurn,
                            isWin = isWin(indexClicked),
                            isTie = false,
                        )
                    )
                }
                return
            }
            else if(isBoardFilled()){
                _sessionUiState.update {
                    it.copy(
                        session = sessionUiState.value.session.copy(
                            winner = null,
                            isWin = false,
                            isTie = true
                        )
                    )
                }
                return
            }
            val newCurrentPlayer = if (sessionUiState.value.session.currentTurn == sessionUiState.value.session.player1) sessionUiState.value.session.player2 else sessionUiState.value.session.player1
            if(newCurrentPlayer!=null){
                _sessionUiState.update {
                    it.copy(
                        session = sessionUiState.value.session.copy(currentTurn = newCurrentPlayer)
                    )
                }
            }
        }
        updateSession()
    }

    private fun getSession(){
        viewModelScope.launch {
            databaseRepository.getSessionByKey(sessionKey = sessionKey).collect{ session ->
                if(session is Resource.Success){
                    _sessionUiState.update {
                        it.copy(session = session.data!!.copy(player1 = _sessionUiState.value.player1))
                    }
                }
            }
        }
        updateSession()
    }
    private fun updateSession(){
        viewModelScope.launch {
            databaseRepository.updateSession(sessionKey,sessionUiState.value.session).collect{ sessionResource ->
                when(sessionResource){
                    is Resource.Error -> {
                        _sessionUiState.update {
                            it.copy(
                                errorMessage = sessionResource.data,
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _sessionUiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null,
                            )
                        }
                    }
                    is Resource.Success -> {
                        getSession()
                    }
                }
            }
        }
    }

    private fun isWin(indexClicked: Int): Boolean {
        val board = sessionUiState.value.session.board
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
        return sessionUiState.value.session.board.all { it != "" }
    }
}
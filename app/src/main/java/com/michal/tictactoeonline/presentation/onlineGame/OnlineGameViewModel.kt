package com.michal.tictactoeonline.presentation.onlineGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnlineGameViewModel(
    private val databaseRepository: DatabaseRepository,
    private val player: Player,
    private val sessionKey:String
): ViewModel() {
    private val _sessionState = MutableStateFlow(OnlineGameUiState())
    val sessionState: StateFlow<OnlineGameUiState> = _sessionState.asStateFlow()

    companion object{
        fun provideFactory(player: Player,sessionKey: String):ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val applicaton = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = applicaton.appContainer.databaseRepository
                    OnlineGameViewModel(dbRepository, player, sessionKey)
                }
            }
        }
    }

    init {

    }

    fun updateBoard(indexClicked: Int){
        if (sessionState.value.session.board[indexClicked] == "" && sessionState.value.session.isWin == null && sessionState.value.session.isTie == null) {
            val oldBoard = sessionState.value.session.board
            val newBoard = oldBoard.toMutableList()
            newBoard[indexClicked] = sessionState.value.session.currentTurn.symbol ?: ""

            _sessionState.update {
                it.copy(
                    session = sessionState.value.session.copy(board = newBoard)
                )
            }

            if (isWin(indexClicked)) {
                _sessionState.update {
                    it.copy(
                        session = sessionState.value.session.copy(
                            winner = sessionState.value.session.currentTurn,
                            isWin = isWin(indexClicked),
                            isTie = false,
                        )
                    )
                }
                return
            }
            else if(isBoardFilled()){
                _sessionState.update {
                    it.copy(
                        session = sessionState.value.session.copy(
                            winner = null,
                            isWin = false,
                            isTie = true
                        )
                    )
                }
                return
            }
            val newCurrentPlayer = if (sessionState.value.session.currentTurn == sessionState.value.session.player1) sessionState.value.session.player2 else sessionState.value.session.player1
            if(newCurrentPlayer!=null){
                _sessionState.update {
                    it.copy(
                        session = sessionState.value.session.copy(currentTurn = newCurrentPlayer)
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
                    _sessionState.update {
                        it.copy(session = session.data!!)
                    }
                }
            }
        }
        updateSession()
    }
    private fun updateSession(){
        viewModelScope.launch {
            databaseRepository.updateSession(sessionKey,sessionState.value.session).collect{ sessionResource ->
                when(sessionResource){
                    is Resource.Error -> {
                        _sessionState.update {
                            it.copy(
                                errorMessage = sessionResource.data,
                                isLoading = false,
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _sessionState.update {
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
        val board = sessionState.value.session.board
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
        return sessionState.value.session.board.all { it != "" }
    }
}
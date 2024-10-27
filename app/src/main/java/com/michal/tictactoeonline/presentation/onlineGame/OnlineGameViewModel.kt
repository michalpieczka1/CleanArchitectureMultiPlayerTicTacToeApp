package com.michal.tictactoeonline.presentation.onlineGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.data.DatabaseRepository
import com.michal.tictactoeonline.data.PlayerRepository
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnlineGameViewModel(
    private val databaseRepository: DatabaseRepository,
    private val playerRepository: PlayerRepository,
    private val sessionKey: String
) : ViewModel() {
    private val _sessionUiState = MutableStateFlow(OnlineGameUiState())
    val sessionUiState: StateFlow<OnlineGameUiState> = _sessionUiState.asStateFlow()

    companion object {
        fun provideFactory(sessionKey: String): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = application.appContainer.databaseRepository
                    val playerRepository = application.appContainer.playerRepository
                    OnlineGameViewModel(
                        databaseRepository = dbRepository,
                        playerRepository = playerRepository,
                        sessionKey = sessionKey
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            async {
                getSession()
                setPlayers()
                setCurrentTurn()
                updateSession()
            }.await()
            launch {
                observeSession()
            }
        }
    }


    private suspend fun observeSession() {
        databaseRepository.observeSessionByKey(sessionKey = sessionKey).collect { session ->
            when (session) {
                is Resource.Error -> {
                    _sessionUiState.update {
                        it.copy(
                            sessionResource = Resource.Error(
                                session.message ?: AppConstants.UNKNOWN_ERROR
                            )
                        )
                    }
                }

                is Resource.Loading -> {
                    _sessionUiState.update {
                        it.copy(
                            sessionResource = Resource.Loading()
                        )
                    }
                }

                is Resource.Success -> {
                    _sessionUiState.update {
                        it.copy(
                            session = session.data ?: Session(),
                            sessionResource = Resource.Success(true)
                        )
                    }
                    println("zaobserwowana sesja")
                }
            }
        }
    }

    private suspend fun getSession() {
        when (val session = databaseRepository.getSessionByKey(sessionKey = sessionKey).last()) {
            is Resource.Error -> {
                _sessionUiState.update {
                    it.copy(
                        sessionResource = Resource.Error(
                            session.message ?: AppConstants.UNKNOWN_ERROR
                        )
                    )
                }
            }

            is Resource.Loading -> {
                _sessionUiState.update {
                    it.copy(
                        sessionResource = Resource.Loading()
                    )
                }
            }

            is Resource.Success -> {
                _sessionUiState.update {
                    it.copy(
                        session = session.data ?: Session(),
                        sessionResource = Resource.Success(true)
                    )
                }
                println("zdobyta sesja ${session.data ?: "pusta"}")
            }
        }

    }

    private suspend fun setPlayers() {
        playerRepository.currentPlayer.first { player ->
            if (_sessionUiState.value.session.player1 == null) {
                _sessionUiState.update {
                    it.copy(
                        session = it.session.copy(
                            player1 = player.copy(symbol = "X"),
                            playerCount = 1
                        )
                    )
                }
                playerRepository.saveSymbol("X")
                println("update 1 player")
            } else {
                _sessionUiState.update {
                    it.copy(
                        session = it.session.copy(
                            player2 = player.copy(symbol = "O"),
                            playerCount = 2
                        )
                    )
                }
                playerRepository.saveSymbol("O")
                println("update 2 player")
            }
            true
        }
    }

    private fun updateSession() {
        databaseRepository.updateSession(sessionKey, sessionUiState.value.session)
        println("zaaktualizowana ${sessionUiState.value.session}")
    }

    private fun updateWinCount() {
        viewModelScope.launch {
            if (sessionUiState.value.session.currentTurn == playerRepository.currentPlayer.first()) {
                val currentWinCount = playerRepository.currentWinCount.first()
                playerRepository.saveWinCount(currentWinCount.toInt() + 1)
            }
        }
    }

    private fun setCurrentTurn() {
        if(sessionUiState.value.session.player2 != null){
            val newCurrentPlayer = if (sessionUiState.value.session.currentTurn == sessionUiState.value.session.player1){
                sessionUiState.value.session.player2
            } else {
                sessionUiState.value.session.player1
            }

            if (newCurrentPlayer != null) {
                _sessionUiState.update {
                    it.copy(
                        session = sessionUiState.value.session.copy(currentTurn = newCurrentPlayer)
                    )
                }
            }
        }
    }

    fun updateBoard(indexClicked: Int) {
        if (sessionUiState.value.session.board[indexClicked] == "" &&
            sessionUiState.value.session.isWin == null &&
            sessionUiState.value.session.isTie == null
            ) {
            viewModelScope.launch {
                val player = playerRepository.currentPlayer.first()

                println("update board session gracz${sessionUiState.value.session.currentTurn} zebrany gracz: $player")

                if (sessionUiState.value.session.currentTurn == player) {

                    val oldBoard = sessionUiState.value.session.board
                    val newBoard = oldBoard.toMutableList()
                    newBoard[indexClicked] = sessionUiState.value.session.currentTurn?.symbol ?: ""

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
                        updateWinCount()
                        updateSession()
                    } else if (isBoardFilled()) {
                        _sessionUiState.update {
                            it.copy(
                                session = sessionUiState.value.session.copy(
                                    winner = null,
                                    isWin = false,
                                    isTie = true
                                )
                            )
                        }
                        updateSession()
                    }else{
                        setCurrentTurn()
                    }
                    updateSession()
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
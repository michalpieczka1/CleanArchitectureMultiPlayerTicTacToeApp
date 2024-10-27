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
    private val _uiState = MutableStateFlow(OnlineGameUiState())
    val uiState: StateFlow<OnlineGameUiState> = _uiState.asStateFlow()

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
                    _uiState.update {
                        it.copy(
                            sessionResource = Resource.Error(
                                session.message ?: AppConstants.UNKNOWN_ERROR
                            )
                        )
                    }
                }

                is Resource.Loading -> {
                    _uiState.update {
                        it.copy(
                            sessionResource = Resource.Loading()
                        )
                    }
                }

                is Resource.Success -> {
                    if(isEnded()){
                        return@collect
                    }

                    _uiState.update {
                        it.copy(
                            session = session.data ?: Session(),
                            sessionResource = Resource.Success(true)
                        )
                    }
                    println("zaobserwowana sesja ${uiState.value.session}")
                }
            }
        }
    }

    private suspend fun getSession() {
        when (val session = databaseRepository.getSessionByKey(sessionKey = sessionKey).last()) {
            is Resource.Error -> {
                _uiState.update {
                    it.copy(
                        sessionResource = Resource.Error(
                            session.message ?: AppConstants.UNKNOWN_ERROR
                        )
                    )
                }
            }

            is Resource.Loading -> {
                _uiState.update {
                    it.copy(
                        sessionResource = Resource.Loading()
                    )
                }
            }

            is Resource.Success -> {
                _uiState.update {
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
            if (_uiState.value.session.player1 == null) {
                _uiState.update {
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
                _uiState.update {
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
        databaseRepository.updateSession(sessionKey, uiState.value.session)
        println("zaaktualizowana ${uiState.value.session}")
    }

    private fun updateWinCount() {
        viewModelScope.launch {
            if (uiState.value.session.currentTurn == playerRepository.currentPlayer.first()) {
                val currentWinCount = playerRepository.currentWinCount.first()
                playerRepository.saveWinCount(currentWinCount.toInt() + 1)
            }
        }
    }

    private fun setCurrentTurn() {
        if (uiState.value.session.player2 != null) {
            val newCurrentPlayer =
                if (uiState.value.session.currentTurn == uiState.value.session.player1) {
                    uiState.value.session.player2
                } else {
                    uiState.value.session.player1
                }

            if (newCurrentPlayer != null) {
                _uiState.update {
                    it.copy(
                        session = uiState.value.session.copy(currentTurn = newCurrentPlayer)
                    )
                }
            }
        }
    }

    fun updateBoard(indexClicked: Int) {
        if (uiState.value.session.board[indexClicked] == "" &&
            uiState.value.session.isWin == null &&
            uiState.value.session.isTie == null
        ) {
            viewModelScope.launch {
                val player = playerRepository.currentPlayer.first()

                println("update board session gracz${uiState.value.session.currentTurn} zebrany gracz: $player")

                if (uiState.value.session.currentTurn == player) {

                    val oldBoard = uiState.value.session.board
                    val newBoard = oldBoard.toMutableList()
                    newBoard[indexClicked] = uiState.value.session.currentTurn?.symbol ?: ""

                    _uiState.update {
                        it.copy(
                            session = uiState.value.session.copy(board = newBoard)
                        )
                    }

                    val hasWon = isWin(indexClicked)
                    val isTie = isBoardFilled() && !hasWon
                    _uiState.update {
                        it.copy(
                            session = uiState.value.session.copy(
                                winner = uiState.value.session.currentTurn,
                                isWin = hasWon,
                                isTie = isTie,
                            )
                        )
                    }
                    if (hasWon) {
                        updateWinCount()
                    } else {
                        setCurrentTurn()
                    }
                    updateSession()
                }
            }
        }
    }

    private fun isEnded(): Boolean {
        return (uiState.value.session.isWin == true|| uiState.value.session.isTie == true)
    }

    private fun isWin(indexClicked: Int): Boolean {
        val board = uiState.value.session.board
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
        return uiState.value.session.board.all { it != "" }
    }
}
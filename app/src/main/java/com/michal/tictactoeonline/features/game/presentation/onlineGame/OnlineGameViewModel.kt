package com.michal.tictactoeonline.features.game.presentation.onlineGame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.data.PlayerRepository
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.data.model.Session
import com.michal.tictactoeonline.common.di.TicTacToeApplication
import com.michal.tictactoeonline.common.util.Resource
import com.michal.tictactoeonline.features.game.data.SessionsDBRepository
import com.michal.tictactoeonline.features.signing.data.PlayersDBRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnlineGameViewModel(
    private val sessionsDBRepository: SessionsDBRepository,
    private val playerRepository: PlayerRepository,
    private val playersDBRepository: PlayersDBRepository,
    private val sessionKey: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnlineGameUiState())
    val uiState: StateFlow<OnlineGameUiState> = _uiState.asStateFlow()

    companion object {
        fun provideFactory(sessionKey: String): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as TicTacToeApplication)
                    val dbRepository = application.appContainer.sessionsDBRepository
                    val playerRepository = application.appContainer.playerRepository
                    val playersDBRepository = application.appContainer.playersDBRepository
                    OnlineGameViewModel(
                        sessionsDBRepository = dbRepository,
                        playerRepository = playerRepository,
                        playersDBRepository,
                        sessionKey = sessionKey
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            getSession()
            setPlayers()
            setCurrentTurn()
            updateSession()

            observeSession()
        }
    }

    private suspend fun observeSession() {
        sessionsDBRepository.observeSessionByKey(sessionKey = sessionKey).collect { session ->
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
                    _uiState.update {
                        if(session.data == null){
                            it.copy(
                                sessionResource = Resource.Error("Getting session returned null")
                            )
                        } else{
                            it.copy(
                                session = session.data,
                                sessionResource = Resource.Success(true)
                            )
                        }
                    }

                }
            }
        }
    }

    private suspend fun getSession() {
        when (val session = sessionsDBRepository.getSessionByKey(sessionKey = sessionKey).last()) {
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
            }
        }

    }

    private suspend fun setPlayers() {
        playerRepository.currentPlayer.first { player ->
            if (_uiState.value.session.player1 == null) {
                _uiState.update {
                    it.copy(
                        session = it.session.copy(
                            player1 = player.copy(
                                symbol = "X",
                                onlineGamesBlocked = true),
                            playerCount = 1
                        )
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        session = it.session.copy(
                            player2 = player.copy(
                                symbol = "O",
                                onlineGamesBlocked = true
                            ),
                            playerCount = 2
                        )
                    )
                }

            }
            playersDBRepository.updatePlayer(playerRepository.currentPlayer.first().copy(onlineGamesBlocked = true))

            true
        }
    }


    private fun updateSession() {
        sessionsDBRepository.updateSession(sessionKey, uiState.value.session)
    }


    private fun setCurrentTurn() {
        if (uiState.value.session.player2 != null) {
            val newCurrentPlayer =
                if (uiState.value.session.currentTurn?.uid == uiState.value.session.player1?.uid) {
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

    fun isEnded(): Boolean {
        return uiState.value.session.win != null && uiState.value.session.tie != null
    }

    fun updateBoard(indexClicked: Int) {
        if (uiState.value.session.board[indexClicked] == "" &&
            uiState.value.session.win == null &&
            uiState.value.session.tie == null
        ) {
            viewModelScope.launch {
                val player = playerRepository.currentPlayer.first()


                if (uiState.value.session.currentTurn?.uid == player.uid) {

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
                    if (hasWon || isTie) {
                        _uiState.update {
                            it.copy(
                                session = uiState.value.session.copy(
                                    winner = uiState.value.session.currentTurn,
                                    win = hasWon,
                                    tie = isTie,
                                )
                            )
                        }
                    }
                    if (hasWon) {
                        if (uiState.value.session.winner?.uid == player.uid) {
                            updatePlayerWinCount()
                        }
                    }
                    if (!isTie && !hasWon) {
                        setCurrentTurn()
                    }
                    updateSession()
                }
            }
        }
    }

    private suspend fun updatePlayerWinCount() {
        val currentWinCount = playerRepository.currentWinCount.first()
        playerRepository.saveWinCount(currentWinCount.toInt() + 1)
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

    private var hasActionExecuted = false
    fun onGoBackClick(onGoBackNavigation: () -> Unit) {
        if (!hasActionExecuted) {
            viewModelScope.launch {
                val playerCount = uiState.value.session.playerCount - 1
                if (uiState.value.session.winner == null) {
                    playerRepository.currentPlayer.first { player ->
                        if (uiState.value.session.player1 == player) {
                            _uiState.update {
                                it.copy(
                                    session = it.session.copy(
                                        win = true,
                                        tie = false,
                                        winner = it.session.player2,  //the other player is the winner
                                        playerLeft = true,
                                        playerCount = playerCount
                                    ),
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    session = it.session.copy(
                                        win = true,
                                        tie = false,
                                        winner = it.session.player1,
                                        playerLeft = true,
                                        playerCount = playerCount
                                    )
                                )
                            }
                        }

                        true
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            session = it.session.copy(
                                playerCount = playerCount
                            )
                        )
                    }
                }
                updateSession()
                if (uiState.value.session.winner == playerRepository.currentPlayer.first()) {
                    updatePlayerWinCount()
                }

                onGoBackNavigation()

                playerRepository.saveInGame(false)

                playersDBRepository.updatePlayer(playerRepository.currentPlayer.first())


                if (uiState.value.session.playerCount == 0) {
                    sessionsDBRepository.removeSession(sessionKey)
                }
            }

            hasActionExecuted = true
        }
    }

    fun onPlayAgain() {
        if (uiState.value.session.playAgainAcceptedCount < 1) {
            _uiState.update {
                it.copy(
                    session = it.session.copy(
                        playAgainAcceptedCount = it.session.playAgainAcceptedCount + 1
                    )
                )
            }
            updateSession()
        } else {
            val player2: Player
            val player1: Player
            if (uiState.value.session.player1?.symbol == "X") {
                player1 = uiState.value.session.player1!!.copy(symbol = "O")
                player2 = uiState.value.session.player2!!.copy(symbol = "X")
            } else {
                player1 = uiState.value.session.player1!!.copy(symbol = "X")
                player2 = uiState.value.session.player2!!.copy(symbol = "O")
            }

            val winnerList: MutableList<Player> = uiState.value.session.winnerList.toMutableList()
            uiState.value.session.winner?.let { winnerList.add(it) }
            _uiState.update {
                it.copy(
                    session = it.session.copy(
                        player1 = player2,
                        player2 = player1,
                        round = it.session.round + 1,
                        win = null,
                        tie = null,
                        winner = null,
                        winnerList = winnerList,
                        board = Session().board,
                        playAgainAcceptedCount = 0,
                        currentTurn = player2
                    )
                )
            }

            updateSession()
        }
    }
}
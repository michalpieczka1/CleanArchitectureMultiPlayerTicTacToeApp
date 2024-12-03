package com.michal.tictactoeonline.features.game.presentation.onlineGame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.data.model.Session
import com.michal.tictactoeonline.common.presentation.otherComposables.ErrorDialog
import com.michal.tictactoeonline.common.util.Resource
import com.michal.ui.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun OnlineGameComposable(
    sessionKey: String,
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onlineGameViewModel: OnlineGameViewModel = viewModel(
        factory = OnlineGameViewModel.provideFactory(sessionKey)
    ),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP || event == Lifecycle.Event.ON_DESTROY) {
                onlineGameViewModel.onGoBackClick(onGoBack)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            println("Lifecycle observer removed 🗑")
        }
    }

    val state = onlineGameViewModel.uiState.collectAsState()
    Surface(modifier = modifier.fillMaxSize()) {


        when (val result = state.value.sessionResource) {
            is Resource.Error -> {
                if (!state.value.session.playerLeft) {
                    ErrorDialog(
                        onErrorDismiss = onGoBack,
                        errorMessage = result.message
                    )
                }
            }

            is Resource.Success, is Resource.Loading -> {

                if (state.value.session.win == true) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        KonfettiView(
                            parties = listOf(
                                Party(
                                    emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(50),
                                    size = listOf(Size.LARGE),
                                )
                            ),
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val context = LocalContext.current
                    var waitingForAllPlayers by remember {
                        mutableStateOf(context.getString(R.string.waiting_for_all_players))
                    }

                    LaunchedEffect(Unit) {
                        var dotCount = 0
                        while (state.value.session.currentTurn == null) {
                            dotCount = (dotCount + 1) % 4
                            waitingForAllPlayers = context.getString(R.string.waiting_for_all_players) + ".".repeat(dotCount)
                            delay(500)
                        }
                    }

                    val displayedText = if (state.value.session.win == true) {
                        stringResource(
                            R.string.is_the_winner,
                            state.value.session.winner?.username ?: "Unknown"
                        )
                    } else if (state.value.session.win == true && state.value.session.playerLeft) {
                        stringResource(R.string.you_win_because_player_left)
                    } else if (state.value.session.tie == true) {
                        stringResource(R.string.it_s_a_tie)
                    } else if (state.value.session.currentTurn == null) {
                        waitingForAllPlayers
                    } else {
                        stringResource(
                            R.string.someone_turn,
                            state.value.session.currentTurn?.username ?: "Unknown"
                        )
                    }

                    Text(
                        text = "Round ${state.value.session.round + 1}",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = displayedText,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .border(6.dp, MaterialTheme.colorScheme.primary),
                    ) {
                        items(state.value.session.board.size) { index ->
                            Button(
                                onClick = { onlineGameViewModel.updateBoard(index) },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .border(3.dp, MaterialTheme.colorScheme.primary)
                                    .aspectRatio(1f)
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                val textColor =
                                    if (state.value.session.board[index] == "X") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                                Text(
                                    text = state.value.session.board[index],
                                    fontSize = 32.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    color = textColor
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { onlineGameViewModel.onGoBackClick(onGoBack) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Go back",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                            )
                        }
                        if (onlineGameViewModel.isEnded() && state.value.session.playerCount == 2) {
                            Button(
                                onClick = { onlineGameViewModel.onPlayAgain() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Play again \n ${state.value.session.playAgainAcceptedCount} / 2",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}

//@Preview
//@PreviewScreenSizes
//@PreviewLightDark
@Composable
fun OnlineGamePreview() {
    val mockViewModel = mockk<OnlineGameViewModel>()

    every { mockViewModel.uiState } returns MutableStateFlow(
        OnlineGameUiState(
            session = Session(
                player1 = Player("player1", symbol = "X"),
                player2 = Player("player2", symbol = "Y"),
                currentTurn = Player("player1", symbol = "X"),
            ),
            sessionResource = Resource.Success(true)
        )
    )
    AppTheme {
        OnlineGameComposable(
            sessionKey = "",
            onGoBack = { /*TODO*/ },
            onlineGameViewModel = mockViewModel
        )
    }
}

//@Preview
@Composable
fun OnlineGameErrorPreview() {
    val mockViewModel = mockk<OnlineGameViewModel>()

    every { mockViewModel.uiState } returns MutableStateFlow(
        OnlineGameUiState(
            sessionResource = Resource.Error("Some error message")
        )
    )
    AppTheme {
        OnlineGameComposable(sessionKey = "", onGoBack = { }, onlineGameViewModel = mockViewModel)
    }
}

@Preview
@PreviewLightDark
@Composable
fun OnlineGameWinPreview() {
    val mockViewModel = mockk<OnlineGameViewModel>()

    every { mockViewModel.uiState } returns MutableStateFlow(
        OnlineGameUiState(
            sessionResource = Resource.Success(true),
            session = Session(winner = Player("test"), win = true, tie = false)
        )
    )

    every { mockViewModel.isEnded() } returns true
    AppTheme {
        OnlineGameComposable(sessionKey = "", onGoBack = { }, onlineGameViewModel = mockViewModel)
    }
}
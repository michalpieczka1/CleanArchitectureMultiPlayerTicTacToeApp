package com.michal.tictactoeonline.presentation.onlineGame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.presentation.otherComposables.ErrorDialog
import com.michal.tictactoeonline.util.Resource
import com.michal.ui.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
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
) {
    val state = onlineGameViewModel.uiState.collectAsState()
    Surface(modifier = modifier.fillMaxSize()) {


        when(val result = state.value.sessionResource){
            is Resource.Error -> {
                ErrorDialog(
                    onErrorDismiss = onGoBack,
                    errorMessage = result.message
                )
            }
            is Resource.Loading -> {

            }
            is Resource.Success -> {
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
                    val displayedText = if (state.value.session.win == true) {
                        stringResource(R.string.is_the_winner, state.value.session.winner?.username ?: "Unknown" )
                    } else if(state.value.session.win == true && state.value.session.playerLeft){
                        stringResource(R.string.you_win_because_player_left)
                    }
                    else if (state.value.session.tie == true) {
                        stringResource(R.string.it_s_a_tie)
                    } else if(state.value.session.currentTurn == null) {
                        stringResource(R.string.waiting_for_all_players)
                    }else{
                        stringResource(R.string.someone_turn, state.value.session.currentTurn?.username ?: "Unknown")
                    }

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
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            items(state.value.session.board.size) { index ->
                                Button(
                                    onClick = { onlineGameViewModel.updateBoard(index) },
                                    shape = RectangleShape,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .border(4.dp, MaterialTheme.colorScheme.inversePrimary)
                                        .fillMaxSize(),
                                    contentPadding = PaddingValues(2.dp)
                                ) {
                                    Text(
                                        text = state.value.session.board[index],
                                        fontSize = 32.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                    )
                                }
                            }
}
                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { onlineGameViewModel.onGoBackClick(onGoBack) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text(
                                text = "Go back",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }

    }

}

@Preview
//@PreviewScreenSizes
//@PreviewLightDark
@Composable
fun OnlineGamePreview(){
    val mockViewModel = mockk<OnlineGameViewModel>()

    every {mockViewModel.uiState } returns MutableStateFlow(
        OnlineGameUiState(
            session = Session(
                player1 = Player("player1", symbol = "X"),
                player2 = Player("player2", symbol = "Y"),
                currentTurn = Player("player1", symbol = "X"),
            ),
            sessionResource = Resource.Success(true)
        )
    )
    AppTheme{
        OnlineGameComposable(sessionKey = "", onGoBack = { /*TODO*/ }, onlineGameViewModel = mockViewModel)
    }
}

@Preview
@Composable
fun OnlineGameErrorPreview(){
    val mockViewModel = mockk<OnlineGameViewModel>()

    every {mockViewModel.uiState } returns MutableStateFlow(
        OnlineGameUiState(
            sessionResource = Resource.Error("Some error message")
        )
    )
    AppTheme{
        OnlineGameComposable(sessionKey = "", onGoBack = { /*TODO*/ }, onlineGameViewModel = mockViewModel)
    }
}
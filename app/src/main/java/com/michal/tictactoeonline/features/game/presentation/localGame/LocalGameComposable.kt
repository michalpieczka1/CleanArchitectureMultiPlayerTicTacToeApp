package com.michal.tictactoeonline.features.game.presentation.localGame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocalGameComposable(
    modifier: Modifier = Modifier,
    localGameViewModel: LocalGameViewModel = viewModel(
        factory = LocalGameViewModel.provideFactory()
    ),
    onGoBack: () -> Unit
) {
    val state = localGameViewModel.uiState.collectAsState()
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {

        if (state.value.isWin == true) {
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
            val displayedText = if (state.value.isWin == true) {
                stringResource(R.string.is_the_winner, state.value.winner?.username ?: "UserName")
            } else if (state.value.isTie == true) {
                stringResource(R.string.it_s_a_tie)
            } else {
                stringResource(R.string.someone_turn, state.value.currentTurn.username)
            }

            Text(
                text = displayedText,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .border(6.dp, MaterialTheme.colorScheme.primary),
                ) {
                    items(state.value.board.size) { index ->
                        Button(
                            onClick = { localGameViewModel.onBoardClicked(index) },
                            shape = RectangleShape,
                            modifier = Modifier
                                .border(3.dp, MaterialTheme.colorScheme.primary)
                                .aspectRatio(1f)
                                .fillMaxSize(),
                            contentPadding = PaddingValues(2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            val textColor = if(state.value.board[index] == "X") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                            Text(
                                text = state.value.board[index],
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = textColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Button(
                    onClick = { onGoBack() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Go back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                if(localGameViewModel.isEnded()){
                    Button(
                        onClick = { localGameViewModel.onPlayAgain() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Play again",
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
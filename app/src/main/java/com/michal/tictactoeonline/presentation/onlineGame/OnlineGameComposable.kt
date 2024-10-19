package com.michal.tictactoeonline.presentation.onlineGame

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.data.model.Player
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnlineGameComposable(
    player: Player,
    sessionKey: String,
    modifier: Modifier = Modifier,
    onlineGameViewModel: OnlineGameViewModel = viewModel(
        factory = OnlineGameViewModel.provideFactory(player,sessionKey)
    ),
) {
    val state = onlineGameViewModel.sessionState.collectAsState()
    Surface(modifier = modifier.fillMaxSize()) {

        if (state.value.session.isWin == true) {
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
            val displayedText = if (state.value.session.isWin == true) {
                stringResource(R.string.is_the_winner, state.value.session.currentTurn.username)
            } else if (state.value.session.isTie == true) {
                stringResource(R.string.it_s_a_tie)
            } else {
                stringResource(R.string.someone_turn, state.value.session.currentTurn.username)
            }

            Text(
                text = displayedText,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                FlowRow(
                    maxItemsInEachRow = 3
                ) {
                    state.value.session.board.indices.forEach {
                        Button(
                            onClick = { onlineGameViewModel.updateBoard(it) },
                            shape = RectangleShape,
                            modifier = Modifier
                                .border(4.dp, MaterialTheme.colorScheme.inversePrimary)
                                .size(128.dp),
                        ) {
                            Text(
                                text = state.value.session.board[it],
                                fontSize = 32.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

        }
    }

}
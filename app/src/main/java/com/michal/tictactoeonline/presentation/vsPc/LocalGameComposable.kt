package com.michal.tictactoeonline.presentation.vsPc

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.data.model.Player

@Composable
fun LocalGameScreen(
    player: Player,
    modifier: Modifier = Modifier,
    viewModel: LocalGameViewModel = viewModel(),
){
    val uiState = viewModel.state.collectAsState()
    Surface {
        Column(
            modifier = modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ){
            Text(
                text = "${uiState.value.currentTurn.username} turn!",
                fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight
            )
            Box(
                modifier = Modifier
                    .padding(64.dp)
                    .border(4.dp, color = Color.Red)
            ) {
                uiState.value.board.indices.forEach {
                    if(it+3 % 3 == 0){
                        Row(modifier = Modifier.border(2.dp,Color.Blue)) {
                            Button(onClick = { viewModel.onBoardClicked(it) }) {
                                Text(text = uiState.value.board[it], fontSize = 32.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
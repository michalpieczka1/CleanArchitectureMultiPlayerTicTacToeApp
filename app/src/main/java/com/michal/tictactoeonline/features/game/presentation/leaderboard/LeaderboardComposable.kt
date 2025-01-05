package com.michal.tictactoeonline.features.game.presentation.leaderboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.util.Resource
import com.michal.ui.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.random.Random

@Composable
fun LeaderboardComposable(
    modifier: Modifier = Modifier,
    viewModel: LeaderboardViewModel = viewModel(
        factory = LeaderboardViewModel.provideFactory()
    ),
    onGoBack: () -> Unit,
){
    val state = viewModel.state.collectAsState()
    Box{
        Button(onClick = onGoBack) {
            Text(text = "go back")
        }
    }
    Column {
        Text(
            text = "Best ${state.value.playerList.count()}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        LazyColumn {
            itemsIndexed(state.value.playerList.sortedByDescending { it.winAmount }){ i, player ->
                ListItem(
                    headlineContent = {
                        Text(text = player.username)
                    },
                    supportingContent = {
                        Text(text = "${player.winAmount} wins")
                    },
                    leadingContent = {
                        Box(
                            Modifier.clip(shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "${i+1}.", modifier = Modifier.padding(4.dp))
                        }
                    }
                )
            }
        }
    }
}
@Preview
@Composable
fun LeaderboardPreview(){
    AppTheme {
        val mockViewModel = mockk<LeaderboardViewModel>()
        val playerList = mutableListOf<Player>()
        for (i in 0..100){
            playerList.add(
                Player(
                    username = java.util.UUID.randomUUID().toString().substring(0..6),
                    winAmount = Random.nextInt(0,100)
                )
            )
        }
        every { mockViewModel.state } returns MutableStateFlow(
            LeaderboardUiState(
                dbResource = Resource.Success(true),
                playerList = playerList
            )
        )
        LeaderboardComposable(
            viewModel = mockViewModel,
            onGoBack = {}
        )
    }
}
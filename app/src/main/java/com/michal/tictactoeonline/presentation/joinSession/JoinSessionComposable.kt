package com.michal.tictactoeonline.presentation.joinSession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.presentation.CardTemplate
import kotlinx.coroutines.flow.Flow

@Composable
fun JoinSessionComposable(
    player: Player,
    modifier: Modifier = Modifier,
    onCloseScreen: (() -> Unit)?,
    onJoined: (String) -> Unit,
    viewModel: JoinSessionViewModel = viewModel(
        factory = JoinSessionViewModel.provideFactory(player)
    )
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        CardTemplate(onClose = onCloseScreen ,title = "Join to a lobby!", Content = {
            JoinSessionContent(viewModel = viewModel, onJoined = onJoined, modifier = modifier)
        })
    }
}

@Composable
fun JoinSessionContent(modifier: Modifier = Modifier, viewModel: JoinSessionViewModel,onJoined: (String) -> Unit,) {
    val state = viewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Session name *", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = state.value.sessionName,
            onValueChange = { viewModel.onSessionNameChange(it) })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Password", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = state.value.password ?: "",
            onValueChange = { viewModel.onPasswordChange(it) })

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "* - required data",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 64.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
        LaunchedEffect(key1 = viewModel.sessionKey) {
            if(viewModel.sessionKey != null){
                onJoined(viewModel.sessionKey!!)
            }
        }
        Button(onClick = {
            viewModel.joinSessionClick()
        }) {
            Text(text = "Join", style = MaterialTheme.typography.titleLarge)
        }
    }
}

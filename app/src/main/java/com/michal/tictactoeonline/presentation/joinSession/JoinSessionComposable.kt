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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.presentation.CardTemplate
import com.michal.tictactoeonline.util.Resource

typealias sessionKey = String

@Composable
fun JoinSessionComposable(
    modifier: Modifier = Modifier,
    onCloseScreen: (() -> Unit)?,
    onJoined: (sessionKey) -> Unit,
    viewModel: JoinSessionViewModel = viewModel(
        factory = JoinSessionViewModel.provideFactory()
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
            onValueChange = { viewModel.onSessionNameChange(it) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Password", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = state.value.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            singleLine = true
        )

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
        Button(onClick = {
            viewModel.joinSessionClick(onJoined)
        }) {
            Text(text = "Join", style = MaterialTheme.typography.titleLarge)
        }
        when(val result = state.value.sessionResource){
            is Resource.Error -> {
                Text(result.message!!) //TODO make custom error text
            }
            is Resource.Loading -> {}
            is Resource.Success -> {
                Text(text = "Udane")
            }
        }
    }
}

package com.michal.tictactoeonline.presentation.publicSessions

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.data.model.Player
import kotlinx.coroutines.delay

@Composable
fun PublicSessionsComposable(
    modifier: Modifier = Modifier,
    player: Player,
    onGoBack: () -> Unit,
    onGoToSession: () -> Unit,
    publicSessionsViewModel: PublicSessionsViewModel = viewModel(
        factory = PublicSessionsViewModel.provideFactory(player)
    )
) {
    val uiState = publicSessionsViewModel.uiState.collectAsState()
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(val state = uiState.value){
                is PublicSessionsUiState.Error -> {
                    Text(
                        text = "Error ${state.message} occurred",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.Red
                    )
                }
                PublicSessionsUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is PublicSessionsUiState.Success -> {
                    if(state.sessions.isNullOrEmpty()){
                        Text(
                            text = "There are no public sessions right now :(",
                            style = MaterialTheme.typography.displaySmall
                        )
                    }else{
                        Text(
                            text = "Join other players!",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        LazyColumn(
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(state.sessions, key = { it.sessionName }){ session ->
                                LaunchedEffect(key1 = session.playerCount) {
                                    if(session.playerCount == 2){
                                        delay(4000)
                                        publicSessionsViewModel.removeSession(session)
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                SessionCard(
                                    session = session,
                                    onClick = { onGoToSession() },
                                    isRemoved = session.playerCount == 2,
                                    modifier = Modifier.animateItem(fadeOutSpec = tween(200,0, FastOutLinearInEasing))
                                ) //TODO add navhost to onClick
                            }
                        }
                    }
                }
            }
        }
    }

}

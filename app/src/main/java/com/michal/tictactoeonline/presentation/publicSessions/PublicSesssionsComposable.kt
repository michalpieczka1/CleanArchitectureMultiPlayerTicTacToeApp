package com.michal.tictactoeonline.presentation.publicSessions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.presentation.joinSession.sessionKey
import com.michal.tictactoeonline.presentation.otherComposables.ErrorDialog
import com.michal.tictactoeonline.util.Resource

typealias sessionKey = String

@Composable
fun PublicSessionsComposable(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onGoToSession: (sessionKey) -> Unit,
    publicSessionsViewModel: PublicSessionsViewModel = viewModel(
        factory = PublicSessionsViewModel.provideFactory()
    )
) {
    val uiState = publicSessionsViewModel.uiState.collectAsState()
    Surface(modifier = modifier
        .fillMaxSize()
        .windowInsetsPadding(insets = WindowInsets.statusBars.also { WindowInsets.systemBars })) {
        Box(contentAlignment = Alignment.TopEnd){
            IconButton(onClick = onGoBack) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "close")
            }
        }
        Column(
            modifier = modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when(val sessionResource = uiState.value.sessionResource){
                is Resource.Error -> {
                    ErrorDialog(
                        onErrorDismiss = onGoBack,
                        errorMessage = sessionResource.message
                    )
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                    if(uiState.value.sessions.isEmpty()){
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
                            items(uiState.value.sessions){ session ->
                                Spacer(modifier = Modifier.height(16.dp))
                                //todo make this animation work
                                AnimatedVisibility(
                                    visible = session.playerCount < 2 || session.playerCount == 2,
                                    exit = fadeOut(
                                        animationSpec = tween(durationMillis = 2000)
                                    )
                                ){
                                    SessionCard(
                                        session = session,
                                        onClick = { publicSessionsViewModel.onSessionJoin(onGoToSession, session.sessionName, session.sessionPassword) },
                                        isRemoved = session.playerCount == 2,
                                        modifier = Modifier.animateItem(fadeOutSpec = tween(200,0, FastOutLinearInEasing))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

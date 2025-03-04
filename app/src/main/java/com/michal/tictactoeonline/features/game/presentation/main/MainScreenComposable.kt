package com.michal.tictactoeonline.features.game.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.common.data.model.Player
import com.michal.tictactoeonline.common.presentation.otherComposables.GameCardComposable
import com.michal.ui.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreenComposable(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onPlayerVsFriend: () -> Unit,
    onCreateSession: () -> Unit,
    onJoinSession: () -> Unit,
    onPublicSessions: () -> Unit,
    onLeaderboard: () -> Unit,
    mainScreenViewModel: MainScreenViewModel = viewModel(
        factory = MainScreenViewModel.provideFactory()
    )
) {
    val playerState = mainScreenViewModel.playerState.collectAsState()
    Scaffold(
        topBar = {
            val topAppBarFont = MaterialTheme.typography.headlineSmall
            val iconSize = (topAppBarFont.fontSize.value * 1.2).dp
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
                        Text(
                            text = playerState.value.username,
                            style = topAppBarFont
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.shadow(
                                4.dp,
                                spotColor = MaterialTheme.colorScheme.primaryContainer,
                                clip = true,
                                shape = RoundedCornerShape(8.dp)
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.trophy_24px),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                        Text(
                            text = "${playerState.value.winAmount}",
                            style = topAppBarFont,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        mainScreenViewModel.onLogOutClick(onLogOut)
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                })
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondary,
                        thickness = 4.dp
                    )
                    Text(
                        text = "OFFLINE",
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondary,
                        thickness = 4.dp
                    )
                }

                FlowRow(horizontalArrangement = Arrangement.Center, verticalArrangement = Arrangement.spacedBy(16.dp), maxItemsInEachRow = 2) {
                    GameCardComposable(
                        iconID = R.drawable.two_players,
                        labelText = stringResource(R.string.play_vs_friend),
                        onClick = onPlayerVsFriend
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondary,
                        thickness = 4.dp
                    )
                    Text(
                        text = "ONLINE",
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f)
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSecondary,
                        thickness = 4.dp
                    )
                }
                val isLocked = playerState.value.onlineGamesBlocked
                if(isLocked){
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth(0.8f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        playerState.value.blockedGamesMessage?.let{ msg ->
                            Text(
                                text = "$msg",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
                FlowRow(horizontalArrangement = Arrangement.Center, verticalArrangement = Arrangement.spacedBy(16.dp), maxItemsInEachRow = 2) {
                    GameCardComposable(
                        iconID = R.drawable.create_lobby,
                        labelText = stringResource(id = R.string.create_session),
                        onClick = onCreateSession,
                        isLocked = isLocked
                    )
                    GameCardComposable(
                        iconID = R.drawable.join_game,
                        labelText = stringResource(id = R.string.join_lobby),
                        onClick = onJoinSession,
                        isLocked = isLocked
                    )
                    GameCardComposable(
                        iconID = R.drawable.list_of_sessions,
                        labelText = stringResource(id = R.string.public_lobby_list),
                        onClick = onPublicSessions,
                        isLocked = isLocked
                    )
                    //TODO add leaderboard
//                    GameCardComposable(
//                        iconID = R.drawable.list_of_sessions,
//                        labelText = stringResource(R.string.leaderboard),
//                        onClick = onLeaderboard,
//                    )
                }
            }
        }
    }
}

@Preview
@PreviewLightDark
@Composable
fun MainScreenComposablePreview() {
    val mockViewModel = mockk<MainScreenViewModel>()
    every { mockViewModel.playerState } returns MutableStateFlow(
        Player("Username", onlineGamesBlocked = true)
    )

    AppTheme {
        MainScreenComposable(
            onPublicSessions = {},
            onJoinSession = {},
            onCreateSession = {},
            onLogOut = {},
            onPlayerVsFriend = {},
            onLeaderboard = {},
            mainScreenViewModel = mockViewModel
        )
    }
}
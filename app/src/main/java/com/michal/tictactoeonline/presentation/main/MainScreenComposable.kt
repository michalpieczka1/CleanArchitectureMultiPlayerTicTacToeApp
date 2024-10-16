package com.michal.tictactoeonline.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R
import com.michal.tictactoeonline.data.model.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    onPlayerVsPc: () -> Unit,
    onCreateSession: () -> Unit,
    onJoinSession: () -> Unit,
    onPublicSessions: () -> Unit,
    player: Player
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = player.username,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                },
                actions = {
                    IconButton(onClick = onLogOut) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                })
        }
    ) { innerPadding ->
        val topPadding = innerPadding.calculateTopPadding()
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
            ){
            Card(
                modifier = Modifier.padding(top = topPadding, start = 16.dp, end = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = onPlayerVsPc, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.play_vs_pc),
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                        )
                    }

                    Button(onClick = onCreateSession, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.create_session),
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                        )
                    }

                    Button(onClick = onJoinSession, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.join_lobby),
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                        )
                    }

                    Button(onClick = onPublicSessions, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.public_lobby_list),
                            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                            fontSize = MaterialTheme.typography.labelLarge.fontSize,
                            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                        )
                    }

                }
            }

        }
    }
}
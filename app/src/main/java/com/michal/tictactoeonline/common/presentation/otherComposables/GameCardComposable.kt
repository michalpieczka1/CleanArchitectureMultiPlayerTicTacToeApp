package com.michal.tictactoeonline.common.presentation.otherComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.tictactoe.R
import com.michal.ui.theme.AppTheme
import com.michal.ui.theme.DarkGradient
import com.michal.ui.theme.LightGradient

@Composable
fun GameCardComposable(
    modifier: Modifier = Modifier,
    iconID: Int,
    labelText: String,
    onClick: () -> Unit,
    isLocked: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.size(164.dp)
    ) {
        Box(
            modifier = Modifier
                .size(128.dp)
                .shadow(8.dp, clip = false, spotColor = MaterialTheme.colorScheme.primary)
                .clip(shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center,
            ) {
            Card(
                onClick = {
                    if (isLocked) {
                    } else onClick()
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxSize()
            ) {
                val gradient =
                    if (isSystemInDarkTheme()) DarkGradient else LightGradient
                Box(
                    modifier = Modifier
                        .background(
                            Brush.radialGradient(colors = gradient)
                        )
                        .size(128.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconID),
                        contentDescription = null,
                        modifier = Modifier.padding(32.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (isLocked) {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock, // Use a lock icon from Material Icons
                        contentDescription = "Locked",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = labelText,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            textAlign = TextAlign.Center,
            maxLines = 2,
        )

    }
}


@Composable
private fun LockedContent(
    content: @Composable() () -> Unit
) {
    Box(modifier = Modifier.blur(8.dp)) {
        content()
    }
}

@Preview
@PreviewLightDark
@Composable
fun GameCardComposablePreview() {
    AppTheme {
        GameCardComposable(
            iconID = R.drawable.two_players,
            labelText = "Play vs friend",
            onClick = {  },

        )
    }
}


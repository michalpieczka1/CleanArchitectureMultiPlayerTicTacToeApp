package com.michal.tictactoeonline.common.presentation.otherComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michal.ui.theme.AppTheme

@Composable
fun CardTemplate(
    modifier: Modifier = Modifier,
    title: String,
    Content: @Composable() () -> Unit,
    onClose: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.tertiary
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(topStart = 64.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 64.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            ) {
            if (onClose != null) {
                Box(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                )
            Spacer(modifier = Modifier.height(32.dp))
            Content()
        }
    }
    }


@Preview(showBackground = true)
@Composable
fun CardTemplatePreview() {
    AppTheme {
        CardTemplate(modifier = Modifier.fillMaxSize(), title = "Tytuł", Content = {
            Column(
                modifier = Modifier.fillMaxWidth(), // Ensure it takes the full width of the parent
                horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
            ) {
                TextField(
                    value = "testowe",
                    onValueChange = {},
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                    Text(
                        text = "Jakiś tekst",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        })
    }
}
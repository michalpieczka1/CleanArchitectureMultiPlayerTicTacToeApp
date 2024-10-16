package com.michal.tictactoeonline.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michal.ui.theme.AppTheme


@Composable
fun CardTemplate(title:String, Content: @Composable() () -> Unit, modifier:Modifier = Modifier){
    Box(modifier = modifier, contentAlignment = Alignment.Center){
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Content()
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun CardTemplatePreview(){
    AppTheme{
        CardTemplate(modifier = Modifier.fillMaxSize(), title = "Tytuł", Content = {
            Test()
        })
    }
}
@Composable
fun Test(){
    TextField(value = "testowe", onValueChange = {})
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {}) {
        Text(text = "Jakiś tekst", fontStyle = MaterialTheme.typography.bodyMedium.fontStyle)
    }
}
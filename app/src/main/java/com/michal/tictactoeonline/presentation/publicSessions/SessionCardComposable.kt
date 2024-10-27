package com.michal.tictactoeonline.presentation.publicSessions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.ui.theme.AppTheme

@Composable
fun SessionCard(modifier: Modifier = Modifier, session: Session, onClick: () -> Unit,isRemoved:Boolean = false) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(4.dp, if(isRemoved) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
        ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                Text(text = session.sessionName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = session.player1?.username ?: "Unknown", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary, maxLines = 1)
                }
            }
            Text(text = "${session.playerCount} / 2", style = MaterialTheme.typography.titleMedium)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview(){
    AppTheme {
        SessionCard(session = Session("test", player1 = Player("Michal")), onClick = { /*TODO*/ })
    }
}
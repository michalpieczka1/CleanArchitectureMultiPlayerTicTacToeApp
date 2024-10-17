package com.michal.tictactoeonline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.presentation.main.MainScreen
import com.michal.tictactoeonline.presentation.vsPc.LocalGameScreen
import com.michal.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
//                    MainScreen(
//                        onLogOut = { /*TODO*/ },
//                        onPlayerVsPc = { /*TODO*/ },
//                        onCreateSession = { /*TODO*/ },
//                        onJoinSession = { /*TODO*/ },
//                        onPublicSessions = { },
//                        player = Player("Nostii"),
//                        modifier = Modifier.fillMaxSize()
//                    )
                LocalGameScreen(player = Player("Nostii"))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TicTacToeScreenPreview() {
    AppTheme {

    }
}
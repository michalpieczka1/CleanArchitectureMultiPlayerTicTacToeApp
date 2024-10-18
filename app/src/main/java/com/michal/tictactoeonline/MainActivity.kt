package com.michal.tictactoeonline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.viewModelFactory
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.presentation.main.MainScreen
import com.michal.tictactoeonline.presentation.publicSessions.PublicSessionsComposable
import com.michal.tictactoeonline.presentation.vsPc.LocalGameScreen
import com.michal.tictactoeonline.presentation.vsPc.LocalGameViewModel
import com.michal.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {

                fun test(session: Session, player: Player) {}
                PublicSessionsComposable(
                    player = Player("Nostii"),
                    onGoBack = { /*TODO*/ },
                    onGoToSession = {})
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun TicTacToeScreenPreview() {
        AppTheme {

        }
    }
}
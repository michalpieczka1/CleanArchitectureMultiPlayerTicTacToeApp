package com.michal.tictactoeonline

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.AppContainer
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.presentation.onlineGame.OnlineGameComposable
import com.michal.tictactoeonline.util.Resource
import com.michal.ui.theme.AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {

            }
        }
    }


@Preview(showBackground = true)
@Composable
fun TicTacToeScreenPreview() {
    AppTheme {

    }
}
    

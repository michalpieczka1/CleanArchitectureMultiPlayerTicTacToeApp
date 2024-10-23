package com.michal.tictactoeonline

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.di.AppContainer
import com.michal.tictactoeonline.di.TicTacToeApplication
import com.michal.tictactoeonline.presentation.main.MainScreenComposable
import com.michal.tictactoeonline.presentation.onlineGame.OnlineGameComposable
import com.michal.tictactoeonline.presentation.register.RegisterComposable
import com.michal.tictactoeonline.presentation.ticTacToeApp.TicTacToeApp
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
                TicTacToeApp(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).windowInsetsPadding(WindowInsets.systemBars))
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
    

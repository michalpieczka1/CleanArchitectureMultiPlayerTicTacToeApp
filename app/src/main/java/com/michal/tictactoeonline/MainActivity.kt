package com.michal.tictactoeonline

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.michal.tictactoeonline.data.TicTacToeRepository
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.di.MyApplication
import com.michal.tictactoeonline.presentation.CardTemplatePreview
import com.michal.tictactoeonline.presentation.main.MainScreen
import com.michal.tictactoeonline.presentation.register.RegisterScreen
import com.michal.tictactoeonline.presentation.register.RegisterViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.michal.tictactoeonline.util.Resource
import com.michal.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                    MainScreen(
                        onLogOut = { /*TODO*/ },
                        onPlayerVsPc = { /*TODO*/ },
                        onCreateSession = { /*TODO*/ },
                        onJoinSession = { /*TODO*/ },
                        onPublicSessions = { },
                        player = Player("Nostii"),
                        modifier = Modifier.fillMaxSize()
                    )
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
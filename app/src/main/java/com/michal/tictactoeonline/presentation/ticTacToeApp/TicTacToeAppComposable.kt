package com.michal.tictactoeonline.presentation.ticTacToeApp

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.michal.tictactoeonline.data.model.Player
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Composable
fun TicTacToeApp(){
    Surface(content = {

    })
}

@Serializable
object RegisterScreen

@Serializable
data class MainScreen(
    val player: Player
)
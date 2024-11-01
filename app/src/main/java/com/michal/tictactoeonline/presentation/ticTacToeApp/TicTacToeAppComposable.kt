package com.michal.tictactoeonline.presentation.ticTacToeApp

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.data.model.Session
import com.michal.tictactoeonline.presentation.createSession.CreateSessionComposable
import com.michal.tictactoeonline.presentation.joinSession.JoinSessionComposable
import com.michal.tictactoeonline.presentation.localGame.LocalGameComposable
import com.michal.tictactoeonline.presentation.main.MainScreenComposable
import com.michal.tictactoeonline.presentation.onlineGame.OnlineGameComposable
import com.michal.tictactoeonline.presentation.publicSessions.PublicSessionsComposable
import com.michal.tictactoeonline.presentation.register.RegisterComposable
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Composable
fun TicTacToeApp(modifier: Modifier = Modifier){
    val navController = rememberNavController()
    Surface(content = {
        NavHost(navController = navController, startDestination = RegisterScreen) {
            composable<RegisterScreen> {
                RegisterComposable(
                    modifier = modifier,
                    onGoToNextScreen = { navController.navigate(MainScreen) }
                )
            }
            composable<MainScreen> {
                MainScreenComposable(
                    onLogOut = { navController.navigate(RegisterScreen) },
                    onPlayerVsPc = { navController.navigate(LocalGameScreen) },
                    onCreateSession = { navController.navigate(CreateSessionScreen) },
                    onJoinSession = { navController.navigate(JoinSessionScreen) },
                    onPublicSessions = { navController.navigate(PublicSessionsScreen) },
                    modifier = modifier
                )
            }
            composable<LocalGameScreen> {
                LocalGameComposable(
                    onGoBack = { navController.popBackStack() },
                    modifier = modifier
                )
            }
            composable<CreateSessionScreen> {
                CreateSessionComposable(
                    onCloseScreen = { navController.popBackStack() },
                    onSessionCreate = { navController.navigate(OnlineGameScreen(sessionKey = it)) }
                )

            }
            composable<JoinSessionScreen> {
                JoinSessionComposable(
                    onCloseScreen = { navController.popBackStack() },
                    onJoined = { navController.navigate(OnlineGameScreen(sessionKey = it)) }
                )
            }
            composable<OnlineGameScreen> { backStackEntry ->
                val sessionKey = backStackEntry.toRoute<OnlineGameScreen>()
                OnlineGameComposable(
                    sessionKey = sessionKey.sessionKey,
                    onGoBack = { navController.navigate(MainScreen) }
                )
            }
            composable<PublicSessionsScreen> {
                PublicSessionsComposable(
                    onGoBack = { navController.navigate(MainScreen) },
                    onGoToSession = { navController.navigate(OnlineGameScreen(sessionKey = it)) }
                )
            }
        }
    })
}

@Serializable
object RegisterScreen

@Serializable
object MainScreen

@Serializable
object LocalGameScreen

@Serializable
object CreateSessionScreen

@Serializable
object JoinSessionScreen

@Serializable
data class OnlineGameScreen(
    val sessionKey: String,
)
@Serializable
object PublicSessionsScreen
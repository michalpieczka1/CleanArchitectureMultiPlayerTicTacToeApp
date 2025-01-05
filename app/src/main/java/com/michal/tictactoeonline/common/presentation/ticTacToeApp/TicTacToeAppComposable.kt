package com.michal.tictactoeonline.common.presentation.ticTacToeApp

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.michal.tictactoeonline.features.game.presentation.createSession.CreateSessionComposable
import com.michal.tictactoeonline.features.game.presentation.joinSession.JoinSessionComposable
import com.michal.tictactoeonline.features.game.presentation.leaderboard.LeaderboardComposable
import com.michal.tictactoeonline.features.game.presentation.localGame.LocalGameComposable
import com.michal.tictactoeonline.features.game.presentation.main.MainScreenComposable
import com.michal.tictactoeonline.features.signing.presentation.register.RegisterComposable
import com.michal.tictactoeonline.features.game.presentation.onlineGame.OnlineGameComposable
import com.michal.tictactoeonline.features.game.presentation.publicSessions.PublicSessionsComposable
import com.michal.tictactoeonline.features.signing.presentation.login.LoginComposable
import kotlinx.serialization.Serializable

@Composable
fun TicTacToeApp(
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel = viewModel(
        factory = TicTacToeViewModel.provideFactory()
    )
){
    val navController = rememberNavController()
    val isUserLogged = viewModel.isUserLogged.value
    if(isUserLogged == null){
        CircularProgressIndicator()
    }else{
        Surface(content = {
            NavHost(
                navController = navController,
                startDestination = if(isUserLogged == true) MainScreen else RegisterScreen,
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }, // Slide from right to left
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth }, // Slide to the left
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(durationMillis = 300))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth }, // Slide in from the left
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) + fadeIn(animationSpec = tween(durationMillis = 300))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth }, // Slide to the right
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) + fadeOut(animationSpec = tween(durationMillis = 300))
                },

                ) {
                composable<RegisterScreen> {
                    RegisterComposable(
                        modifier = modifier,
                        onGoToNextScreen = { navController.navigate(MainScreen) },
                        onGoToLoginScreen = { navController.navigate(LoginScreen) }
                    )
                }
                composable<LoginScreen> {
                    LoginComposable(
                        modifier = modifier,
                        onGoToNextScreen = { navController.navigate(MainScreen) },
                        onGoToRegisterScreen = { navController.navigate(RegisterScreen) }
                    )
                }
                composable<MainScreen> {
                    MainScreenComposable(
                        onLogOut = { navController.navigate(RegisterScreen) },
                        onPlayerVsFriend = { navController.navigate(LocalGameScreen) },
                        onCreateSession = { navController.navigate(CreateSessionScreen) },
                        onJoinSession = { navController.navigate(JoinSessionScreen) },
                        onPublicSessions = { navController.navigate(PublicSessionsScreen) },
                        onLeaderboard = {navController.navigate(LeaderboardScreen)},
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
                composable<LeaderboardScreen> {
                    LeaderboardComposable(
                        onGoBack = { navController.navigate(MainScreen) }
                    )
                }
            }
        })

    }
}

@Serializable
object RegisterScreen

@Serializable
object LoginScreen

@Serializable
object MainScreen

@Serializable
object LocalGameScreen

@Serializable
object CreateSessionScreen

@Serializable
object JoinSessionScreen

@Serializable
object LeaderboardScreen

@Serializable
data class OnlineGameScreen(
    val sessionKey: String,
)
@Serializable
object PublicSessionsScreen
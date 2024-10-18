package com.michal.tictactoeonline.presentation.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.presentation.CardTemplate

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(),
    onGoToNextScreen : (Player) -> Unit
){
    val state = registerViewModel.state.collectAsState()
    //TODO add savedStateHandle
    CardTemplate(title = "Hi, register pls :)", modifier = modifier.fillMaxSize(), Content = {
        Text(
            text = "Nickname",
            style = MaterialTheme.typography.labelLarge
        )
        TextField(value = state.value.username, onValueChange = { registerViewModel.onUsernameChange(it) })

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Password",
            style = MaterialTheme.typography.labelLarge
        )
        TextField(value = state.value.password, onValueChange = { registerViewModel.onPasswordChange(it) })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            registerViewModel.onRegisterClick()
            onGoToNextScreen(registerViewModel.player)
        }) {
            Text(text = "Register",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    })
}
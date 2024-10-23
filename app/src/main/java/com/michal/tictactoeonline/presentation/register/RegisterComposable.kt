package com.michal.tictactoeonline.presentation.register

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.michal.tictactoeonline.data.model.Player
import com.michal.tictactoeonline.presentation.CardTemplate

@Composable
fun RegisterComposable(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory()
    ),
    onGoToNextScreen : () -> Unit,
){
    val state = registerViewModel.state.collectAsState()

    if(state.value.isUserAlreadyLogged){
        onGoToNextScreen()
    }else{
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
                onGoToNextScreen()
            }) {
                Text(text = "Register",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        })
    }
}
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
import com.michal.tictactoeonline.presentation.CardTemplate

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(),
    onGoToNextScreen : () -> Unit
){
    val state = registerViewModel.state.collectAsState()

    CardTemplate(title = "Hi, register pls :)", modifier = modifier.fillMaxSize(), Content = {
        Text(
            text = "Nickname",
            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
        )
        TextField(value = state.value.username, onValueChange = { registerViewModel.onUsernameChange(it) })

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Password",
            fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
            fontSize = MaterialTheme.typography.labelLarge.fontSize,
            fontWeight = MaterialTheme.typography.labelLarge.fontWeight
        )
        TextField(value = state.value.password, onValueChange = { registerViewModel.onPasswordChange(it) })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { registerViewModel.onRegisterClick() }) {
            Text(text = "Register",
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight
            )
        }
    })
}
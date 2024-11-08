package com.michal.tictactoeonline.presentation.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.presentation.otherComposables.CardTemplate

@Composable
fun RegisterComposable(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory()
    ),
    onGoToNextScreen: () -> Unit,
) {
    val state = registerViewModel.state.collectAsState()

    if (state.value.isUserAlreadyLogged) {
        onGoToNextScreen()
    } else {
        val usernameError = state.value.usernameError != null && state.value.isButtonClicked

        CardTemplate(title = "Hi, register pls :)", modifier = modifier.fillMaxSize(), Content = {
            Text(
                text = "Username *",
                style = MaterialTheme.typography.labelLarge
            )
            TextField(
                value = state.value.username,
                onValueChange = { registerViewModel.onUsernameChange(it) },
                isError = usernameError
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Password",
                style = MaterialTheme.typography.labelLarge
            )
            TextField(
                value = state.value.password,
                onValueChange = { registerViewModel.onPasswordChange(it) })
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "* - required data",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 64.dp)
                )
            }
            if (usernameError) {
                Text(
                    text = state.value.usernameError!!,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                registerViewModel.onRegisterClick(onGoToNextScreen)
            }) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        })
    }
}
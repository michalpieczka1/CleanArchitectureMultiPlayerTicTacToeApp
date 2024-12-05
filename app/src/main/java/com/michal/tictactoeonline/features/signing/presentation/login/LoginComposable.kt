package com.michal.tictactoeonline.features.signing.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.common.presentation.otherComposables.CardTemplate
import com.michal.ui.theme.AppTheme
import com.michal.ui.theme.LoginColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LoginComposable(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.provideFactory()
    ),
    onGoToNextScreen: () -> Unit,
    onGoToRegisterScreen: () -> Unit,
) {
    val state = loginViewModel.state.collectAsState()

    if (state.value.isUserAlreadyLogged) {
        onGoToNextScreen()
    } else {
        val isUsernameError = state.value.usernameError != null && state.value.isButtonClicked

        Column(
            modifier = modifier
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.align(Alignment.End)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Welcome Back 👋",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "Log in to join the game",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            CardTemplate(
                title = "Log In",
                containerColor = LoginColor,
                modifier = Modifier
                    .fillMaxWidth(),
                Content = {
                        Text(
                            text = "Username",
                            style = MaterialTheme.typography.labelLarge
                        )
                        OutlinedTextField(
                            value = state.value.username,
                            onValueChange = { loginViewModel.onUsernameChange(it) },
                            isError = isUsernameError,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                disabledBorderColor = MaterialTheme.colorScheme.secondary,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.labelLarge
                        )
                        OutlinedTextField(
                            value = state.value.password,
                            onValueChange = { loginViewModel.onPasswordChange(it) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.secondary,
                                unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                                disabledBorderColor = MaterialTheme.colorScheme.secondary,
                                errorBorderColor = MaterialTheme.colorScheme.error,
                                cursorColor = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                        if (isUsernameError) {
                            Text(
                                text = state.value.usernameError!!,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    loginViewModel.onLoginClick(onGoToNextScreen)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                modifier = Modifier.fillMaxWidth(0.8f)
                            ) {
                                Text(
                                    text = "SIGN UP",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }

                        }
                })

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account yet?",
                )
                TextButton(onClick = onGoToRegisterScreen) {
                    Text(
                        text = "Join now",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


        }

    }
}


@Preview(showBackground = true)
@PreviewLightDark
@Composable
fun LoginComposablePreview() {
    AppTheme {
        val mockViewModel = mockk<LoginViewModel>()
        every { mockViewModel.state } returns MutableStateFlow(
            LoginUiState(
                username = "uzytkownik111"
            )
        )
        LoginComposable(
            loginViewModel = mockViewModel,
            onGoToNextScreen = {},
            onGoToRegisterScreen = {})
    }
}
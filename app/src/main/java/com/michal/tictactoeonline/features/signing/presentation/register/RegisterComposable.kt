package com.michal.tictactoeonline.features.signing.presentation.register

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
import com.michal.ui.theme.RegisterColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RegisterComposable(
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory()
    ),
    onGoToNextScreen: () -> Unit,
    onGoToLoginScreen: () -> Unit,
) {
    val state = registerViewModel.state.collectAsState()

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
                        text = "Welcome 👋",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "Signup to create your account",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            CardTemplate(title = "Sign Up",containerColor = RegisterColor, modifier = Modifier
                .fillMaxWidth(),
                Content = {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.labelLarge
                    )
                    OutlinedTextField(
                        value = state.value.username,
                        onValueChange = { registerViewModel.onUsernameChange(it) },
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
                        ),

                        )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.labelLarge
                    )
                    OutlinedTextField(
                        value = state.value.password,
                        onValueChange = { registerViewModel.onPasswordChange(it) },
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
                                registerViewModel.onRegisterClick(onGoToNextScreen)
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
                        text = "Already have an account?",
                    )
                    TextButton(onClick = onGoToLoginScreen) {
                        Text(
                            text = "Log in",
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
fun RegisterComposablePreview() {
    AppTheme {
        val mockViewModel = mockk<RegisterViewModel>()
        every { mockViewModel.state } returns MutableStateFlow(
            RegisterUiState(
                username = "uzytkownik111"
            )
        )
        RegisterComposable(
            registerViewModel = mockViewModel,
            onGoToNextScreen = {},
            onGoToLoginScreen = {})
    }
}
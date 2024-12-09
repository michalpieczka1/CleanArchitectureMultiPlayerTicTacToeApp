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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.common.presentation.otherComposables.CardTemplate
import com.michal.tictactoeonline.common.presentation.otherComposables.CustomOutlinedPasswordTextField
import com.michal.tictactoeonline.common.presentation.otherComposables.CustomOutlinedTextField
import com.michal.ui.theme.AppTheme
import com.michal.ui.theme.RegisterColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
//todo show errors and if user set password show dialog if he really wants to create acc without password
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

        var passwordHidden by remember {
            mutableStateOf(true)
        }
        Column(
            modifier = modifier
                .padding(32.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.align(Alignment.End)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Welcome 👋",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.tertiary
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
                    CustomOutlinedTextField(
                        value = state.value.username,
                        onValueChange = { registerViewModel.onUsernameChange(it) },
                        isError = isUsernameError,
                        label = {
                            Text(
                                text = "Username",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary) }
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

                    CustomOutlinedPasswordTextField(
                        value = state.value.password,
                        onValueChange = { registerViewModel.onPasswordChange(it) },
                        label = {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                registerViewModel.onRegisterClick(onGoToNextScreen)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(
                                text = "SIGN UP",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSecondary,
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
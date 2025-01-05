package com.michal.tictactoeonline.features.signing.presentation.login

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
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
import com.michal.ui.theme.DarkGradient
import com.michal.ui.theme.DarkOnPrimaryContainer
import com.michal.ui.theme.DarkPrimaryColor
import com.michal.ui.theme.LightGradient
import com.michal.ui.theme.LoginColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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

        var emojiFloat by remember { mutableFloatStateOf(0f) }
        val durationMillis = 500
        val emojiAngle by animateFloatAsState(
            targetValue = emojiFloat,
            animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
            label = "Emoji rotation"
        )


        val welcomeTextOffset = remember { Animatable(0f) }
        var welcomeTextWidth by remember { mutableFloatStateOf(0f) }

        val welcomeTextBrush = Brush.horizontalGradient(
            colors = if(isSystemInDarkTheme()) DarkGradient else LightGradient.reversed(),
            startX = welcomeTextOffset.value,
            endX = welcomeTextOffset.value * 2
        )

        val focusManager = LocalFocusManager.current

        LaunchedEffect(Unit) {
            launch {
                welcomeTextOffset.animateTo(
                    targetValue = welcomeTextWidth / 2,
                    animationSpec = tween(
                        durationMillis = durationMillis * 5,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
            launch {
                repeat(2){
                    emojiFloat = 25f
                    delay(timeMillis = durationMillis.toLong())
                    emojiFloat = -25f
                    delay(timeMillis = durationMillis.toLong())
                }
                emojiFloat = 0f
            }
        }

        Column(
            modifier = modifier
                .imePadding()
                .padding(horizontal = 32.dp, vertical = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Box(modifier = Modifier.align(Alignment.End)) {
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = stringResource(R.string.welcome_back),
                            style = MaterialTheme.typography.displayLarge.copy(
                                brush = welcomeTextBrush
                            ),
                            color = Color.Unspecified,
                            modifier = Modifier.onGloballyPositioned { layoutCoordinates ->
                                welcomeTextWidth = layoutCoordinates.size.width.toFloat()
                            }
                        )
                        Text(
                            text = "👋",
                            style = MaterialTheme.typography.displayLarge,
                            modifier = Modifier
                                .rotate(emojiAngle)
                                .padding(start = 8.dp)
                        )
                    }
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
                    CustomOutlinedTextField(
                        value = state.value.username,
                        onValueChange = { loginViewModel.onUsernameChange(it) },
                        isError = isUsernameError,
                        label = {
                            Text(
                                text = "Username",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        modifier = Modifier.fillMaxWidth(),
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
                        onValueChange = { loginViewModel.onPasswordChange(it) },
                        label = {
                            Text(
                                text = "Password",
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                loginViewModel.onLoginClick(onGoToNextScreen)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            Text(
                                text = "LOG IN",
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
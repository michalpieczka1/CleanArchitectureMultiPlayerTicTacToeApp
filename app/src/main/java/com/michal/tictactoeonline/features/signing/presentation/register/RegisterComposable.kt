package com.michal.tictactoeonline.features.signing.presentation.register

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
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
import com.michal.tictactoeonline.common.presentation.otherComposables.WarningDialog
import com.michal.ui.theme.AppTheme
import com.michal.ui.theme.DarkGradient
import com.michal.ui.theme.LightGradient
import com.michal.ui.theme.RegisterColor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

//TODO manage errors better
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
        colors = if (isSystemInDarkTheme()) DarkGradient else LightGradient.reversed(),
        startX = welcomeTextOffset.value,
        endX = welcomeTextOffset.value * 2
    )

    val focusManager = LocalFocusManager.current
    val focusRequester = remember {
        FocusRequester()
    }
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
            repeat(2) {
                emojiFloat = 25f
                delay(timeMillis = durationMillis.toLong())
                emojiFloat = -25f
                delay(timeMillis = durationMillis.toLong())
            }
            emojiFloat = 0f
        }
    }

    val isUsernameError = state.value.usernameError != null && state.value.isButtonClicked

    Column(
        modifier = modifier
            .padding(32.dp)
            .fillMaxHeight()
            .imePadding(),
        verticalArrangement = Arrangement.Center,
    ) {
        if(state.value.showWarningDialog){
            WarningDialog(
                onDismiss = { registerViewModel.onRegisterClick(onGoToNextScreen) },
                onConfirm = {
                    focusRequester.requestFocus()
                    registerViewModel.hideWarningDialog()
                },
                onDismissText = "Ignore",
                onConfirmText = "Add password",
                title = "No password set",
                description = "While setting a password is optional, we highly recommend creating one to enhance your account's security.",
                headerEmoji = "⚠\uFE0F"
            )
        }

        Box(modifier = Modifier.align(Alignment.End)) {
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.welcome),
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
                    text = "Signup to create your account",
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        CardTemplate(title = "Sign Up", containerColor = RegisterColor, modifier = Modifier
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
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountBox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = state.value.password,
                    onValueChange = { registerViewModel.onPasswordChange(it) },
                    label = {
                        Text(
                            text = "Password",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Password is not necessary",
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            style = MaterialTheme.typography.titleSmall
                        )
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
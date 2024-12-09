package com.michal.tictactoeonline.features.game.presentation.createSession

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michal.tictactoeonline.common.AppConstants
import com.michal.tictactoeonline.common.presentation.otherComposables.CardTemplate
import com.michal.tictactoeonline.common.presentation.otherComposables.CustomOutlinedPasswordTextField
import com.michal.tictactoeonline.common.presentation.otherComposables.CustomOutlinedTextField
import com.michal.tictactoeonline.common.presentation.otherComposables.ErrorDialog
import com.michal.tictactoeonline.common.util.Resource
import com.michal.ui.theme.AppTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

typealias sessionKey = String

@Composable
fun CreateSessionComposable(
    modifier: Modifier = Modifier,
    onCloseScreen: () -> Unit,
    onSessionCreate: (sessionKey) -> Unit,
    viewModel: CreateSessionViewModel = viewModel(
        factory = CreateSessionViewModel.provideFactory()
    )
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box(contentAlignment = Alignment.Center) {
            CardTemplate(
                onClose = onCloseScreen,
                title = "Create lobby!",
                modifier = Modifier.padding(32.dp),
                Content = {
                    CreateSessionContent(
                        viewModel = viewModel,
                        modifier = modifier,
                        onSessionCreate = onSessionCreate,
                        onErrorDismiss = onCloseScreen
                    )
                })
        }
    }
}

@Composable
fun CreateSessionContent(
    modifier: Modifier = Modifier,
    viewModel: CreateSessionViewModel,
    onSessionCreate: (sessionKey) -> Unit,
    onErrorDismiss: () -> Unit
) {
    val state = viewModel.uiState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        CustomOutlinedTextField(
            value = state.value.sessionName,
            onValueChange = { viewModel.onSessionNameChange(it) },
            label = {
                Text(text = "Session name", style = MaterialTheme.typography.titleMedium)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.AccountBox, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomOutlinedPasswordTextField(
            value = state.value.sessionPassword,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = {
                Text(text = "Password", style = MaterialTheme.typography.titleMedium)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondary)
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth()){
            Button(
                onClick = { viewModel.createSessionClick(onSessionCreate) },
                modifier = Modifier.fillMaxWidth(0.8f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = "Create",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        when(val result = state.value.resultResource){
            is Resource.Error -> {
                ErrorDialog(
                    onErrorDismiss = onErrorDismiss,
                    onConfirm = {
                        viewModel.onTryAgain()
                    },
                    errorMessage = result.message
                )
            }
            is Resource.Loading -> {}
            is Resource.Success -> Log.i(AppConstants.SUCCESS_TAG,"Successfully created session")
        }
    }
}

@Preview
@Composable
fun CreateSessionErrorPreview(){
    AppTheme {
        val mockViewModel = mockk<CreateSessionViewModel>()
        every { mockViewModel.uiState } returns MutableStateFlow(
            CreateSessionUiState(
                resultResource = Resource.Error("Error")
            )
        )
        CreateSessionComposable(onCloseScreen = {  }, onSessionCreate = {}, viewModel = mockViewModel)
    }
}
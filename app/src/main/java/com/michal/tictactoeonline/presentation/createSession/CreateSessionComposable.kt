package com.michal.tictactoeonline.presentation.createSession

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tictactoe.R
import com.michal.tictactoeonline.AppConstants
import com.michal.tictactoeonline.presentation.otherComposables.CardTemplate
import com.michal.tictactoeonline.presentation.otherComposables.ErrorDialog
import com.michal.tictactoeonline.util.Resource
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
        CardTemplate(onClose = onCloseScreen, title = "Create lobby!", Content = {
            CreateSessionContent(
                viewModel = viewModel,
                modifier = modifier,
                onSessionCreate = onSessionCreate,
                onErrorDismiss = onCloseScreen
                )
        })
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Session name *", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = state.value.sessionName,
            onValueChange = { viewModel.onSessionNameChange(it) })

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Password", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = state.value.password,
            onValueChange = { viewModel.onPasswordChange(it) })

        Spacer(modifier = Modifier.height(8.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            viewModel.createSessionClick(onSessionCreate)
        }) {
            Text(text = "Create", style = MaterialTheme.typography.titleLarge)
        }
        when(val result = state.value.resultResource){
            is Resource.Error -> {
                ErrorDialog(
                    onErrorDismiss = onErrorDismiss,
                    onConfirm = { viewModel.createSessionClick(onSessionCreate) },
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
        CreateSessionComposable(onCloseScreen = { /*TODO*/ }, onSessionCreate = {}, viewModel = mockViewModel)
    }
}
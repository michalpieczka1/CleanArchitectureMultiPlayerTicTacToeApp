package com.michal.tictactoeonline.presentation.otherComposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tictactoe.R
import com.michal.tictactoeonline.AppConstants

@Composable
fun ErrorDialog(
    onErrorDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    errorMessage: String?
){
    AlertDialog(
        onDismissRequest = onErrorDismiss,
        dismissButton = {
            TextButton(onClick = onErrorDismiss) {
                Text(text = "Go back")
            }
        },
        confirmButton = {
            if(onConfirm != null){
                TextButton(onConfirm) {
                    Text(text = "Try again")
                }
            }
        },
        title = {
            Text(
                text = stringResource(R.string.error_occurred),
                style = MaterialTheme.typography.headlineMedium
            )
        },
        icon = {
            Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
        },
        text = {
            Text(
                text = errorMessage ?: AppConstants.UNKNOWN_ERROR,
                style = MaterialTheme.typography.bodySmall
            )
        }
    )
}
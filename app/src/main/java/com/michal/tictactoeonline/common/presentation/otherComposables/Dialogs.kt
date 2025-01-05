package com.michal.tictactoeonline.common.presentation.otherComposables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tictactoe.R
import com.michal.tictactoeonline.common.AppConstants
import com.michal.ui.theme.AppTheme

@Composable
fun WarningDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDismissText: String,
    onConfirmText: String,
    headerImageID: Int? = null
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(onClick = onDismiss) {
                                Text(
                                    text = onDismissText,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            ElevatedButton(
                                onClick = onConfirm,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth(),

                                ) {
                                Text(
                                    text = onConfirmText,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                }

            }
            if (headerImageID != null) {
                Image(
                    painter = painterResource(id = headerImageID),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}

@Composable
fun WarningDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onDismissText: String,
    onConfirmText: String,
    headerEmoji: String? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(64.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ElevatedButton(
                                onClick = onConfirm,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth(),

                            ) {
                                Text(
                                    text = onDismissText,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Button(onClick = onDismiss) {
                                Text(
                                    text = onConfirmText,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                }

            }
            if (headerEmoji != null) {
                Text(text = headerEmoji, fontSize = 64.sp, modifier = Modifier.align(Alignment.TopCenter))
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}

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

@Preview()
@Composable
fun WarningDialogPreview() {
    AppTheme {
        WarningDialog(
            onDismiss = { /*TODO*/ },
            onConfirm = { /*TODO*/ },
            onDismissText = "Register",
            onConfirmText = "Add password",
            title = "No password set",
            description = "Even tho password is not necessary we recommend to set it anyway",
            headerEmoji = "⚠\uFE0F"
        )
    }
}
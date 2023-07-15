package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.utils.EmailUtils

@Preview
@Composable
fun FeedbackDialog(closeDialog : () -> Unit = {}) {
    val context = LocalContext.current
    AlertDialog(
        title = {
            Text(stringResource(id = R.string.feedback))
                },
        icon = {Icon(Icons.Default.Email, contentDescription = null)},
        confirmButton = {
            TextButton(
                enabled = true,
                onClick = {
                    EmailUtils.sendFeedbackEmail(context)
                    closeDialog()
                }
            ) {
                Text(stringResource(id = R.string.send_email))
            }
        },
        text = {
               Text(stringResource(id = R.string.feedback_request))
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        dismissButton = {
            TextButton(onClick = { closeDialog() }) {
                Text(stringResource(id = R.string.cancel))
            }

        },
        onDismissRequest = { closeDialog() })
}
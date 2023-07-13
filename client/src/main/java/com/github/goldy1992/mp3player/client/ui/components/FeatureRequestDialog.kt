package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.icons.GithubIcon
import com.github.goldy1992.mp3player.client.ui.utils.EmailUtils

private const val FEATURE_REQUEST_URL = "https://github.com/goldy1992/Mp3Player/issues/new?assignees=goldy1992&labels=feature&projects=&template=feature_request.md&title=[FEATURE]+-+Short+description+of+idea"
@Preview
@Composable
fun FeatureRequestDialog(
    darkMode : Boolean = false,
    closeDialog : () -> Unit = {}
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val featureRequestText = stringResource(id = R.string.request_feature)
    AlertDialog(
        title = { Text(featureRequestText) },
        confirmButton = {
            TextButton(
                onClick = {
                    closeDialog()}
            ) {
                Text(stringResource(id = R.string.done))
            }
        },
        text = {
            Column {

                ListItem(
                    modifier = Modifier.clickable {
                        uriHandler.openUri(FEATURE_REQUEST_URL)
                    },
                    leadingContent = { GithubIcon(modifier = Modifier.size(24.dp), isDarkMode = darkMode)},
                    headlineContent = {
                    Text(text = "GitHub",  maxLines = 1)
                })
                Divider()
                ListItem(
                    modifier = Modifier.clickable {
                        EmailUtils.sendBugReportEmail(context)
                    },
                    leadingContent = {
                        Icon(Icons.Default.Email,
                            contentDescription = "send email",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    headlineContent = {
                        Text(text = "Send email",  maxLines = 1)
                    })
            }
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,

        onDismissRequest = { closeDialog() })
}


package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.icons.GithubIcon

const val GITHUB_REPO_URL = "https://github.com/goldy1992/Mp3Player"

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AboutDialog(
    closeDialog : () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val aboutTitle = stringResource(id = R.string.about)
    AlertDialog(
        title = { Text(aboutTitle) },
        confirmButton = {
            GithubIcon(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { uriHandler.openUri(GITHUB_REPO_URL) }
            )
        },
        text = {
            val aboutDescription = stringResource(id = R.string.about_description)
            Text(aboutDescription)
        },
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        dismissButton = {
            IconButton(onClick = { closeDialog() }) {
                Icon(Icons.Outlined.Close, contentDescription = stringResource(id = R.string.close))
            }
        },
        onDismissRequest = { closeDialog() }
    )
}
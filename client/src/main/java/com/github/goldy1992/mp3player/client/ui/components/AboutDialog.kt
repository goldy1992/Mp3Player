package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R

const val GITHUB_REPO = "https://github.com/goldy1992/Mp3Player"
val githubRepoLink = Uri.parse(GITHUB_REPO)

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AboutDialog(
    darkMode : Boolean = true,
    closeDialog : () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current
    val aboutTitle = stringResource(id = R.string.about)
    AlertDialog(
        title = { Text(aboutTitle) },
        confirmButton = {
            Image(
                painterResource(id = if (darkMode) R.drawable.github_mark_white else R.drawable.github_mark),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { uriHandler.openUri(GITHUB_REPO) })
        },
        text = {
            Surface(
                modifier = Modifier
        //        .fillMaxWidth()
                .height(150.dp),
                color = MaterialTheme.colorScheme.surfaceVariant) {
                Column(
                    Modifier
                        .padding(4.dp)
                //        .fillMaxSize()
                ) {
                val aboutDescription = stringResource(id = R.string.about_description)
                    Text(aboutDescription)
                }
            }

        },
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        dismissButton = {
            IconButton(onClick = { closeDialog() }) {
                Icon(Icons.Outlined.Close, contentDescription = "")
            }
        },
        onDismissRequest = { closeDialog() }
    )
}
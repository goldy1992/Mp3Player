package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingDialog(darkMode : Boolean = false,
                 closeDialog : () -> Unit = {}
) {
    AlertDialog(
        title = { Text("How is it?")},
        confirmButton = {},
        text = {
            Column {
                Rating()
            }

        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        dismissButton = {
            IconButton(onClick = { closeDialog() }) {
                Icon(Icons.Outlined.Close, contentDescription = "")
            }

        },
        onDismissRequest = { /*TODO*/ })
}
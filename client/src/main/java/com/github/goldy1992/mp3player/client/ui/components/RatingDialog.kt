package com.github.goldy1992.mp3player.client.ui.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.ui.utils.RatingUtils.submit

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingDialog(
    onSubmit : () -> Unit = {},
    closeDialog : (Int?) -> Unit = {_->}
) {
    val context = LocalContext.current
    var currentRating by remember { mutableStateOf(5) } // default to 5 stars
    AlertDialog(
        title = { Text("How is it?")},
        confirmButton = {
            TextButton(
                enabled = currentRating >= 1,
                onClick = {
                    closeDialog(currentRating)}
            ) {
                Text("Submit")
            }
        },
        text = {
            Rating(currentRating = currentRating) {
                currentRating = it
            }
        },
        textContentColor = MaterialTheme.colorScheme.onSurface,
        dismissButton = {
            TextButton(onClick = { closeDialog(null) }) {
                Text("Cancel")
            }

        },
        onDismissRequest = { closeDialog(null) })
}
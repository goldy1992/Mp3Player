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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RatingDialog(
    closeDialog : (Int?) -> Unit = {_->}
) {
    var currentRating by remember { mutableStateOf(5) } // default to 5 stars
    AlertDialog(
        title = { Text(stringResource(id = R.string.rating_dialog_title))},
        confirmButton = {
            TextButton(
                enabled = currentRating >= 1,
                onClick = {
                    closeDialog(currentRating)}
            ) {
                Text(stringResource(id = R.string.submit))
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
                Text(stringResource(id = R.string.cancel))
            }

        },
        onDismissRequest = { closeDialog(null) })
}
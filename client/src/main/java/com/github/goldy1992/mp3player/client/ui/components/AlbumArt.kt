package com.github.goldy1992.mp3player.client.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import org.apache.commons.lang3.StringUtils.isEmpty

private const val LOG_TAG = "AlbumArtAsync"

@Composable
fun AlbumArtAsync(uri : Uri,
                  contentDescription: String,
                  modifier : Modifier = Modifier
) {
    Log.v(LOG_TAG, "AlbumArtAsync() Invoked with Uri: ${uri.path}")

    if (isEmpty(uri.path)) {
        Icon(
            Icons.Filled.Error,
            contentDescription = "error-$contentDescription",
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = uri,
            contentDescription = contentDescription,
            modifier = modifier.clip(RoundedCornerShape(10f))
        )
    }
}
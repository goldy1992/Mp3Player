package com.github.goldy1992.mp3player.client.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.github.goldy1992.mp3player.client.R
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
        SubcomposeAsyncImage(
            model = uri,
            contentDescription = contentDescription,
            modifier = modifier.clip(RoundedCornerShape(10f)),
        ) {
            when (painter.state) {
                is AsyncImagePainter.State.Loading -> {
                    Log.v(LOG_TAG, "AlbumArtAsync() PainterState: LOADING")
                    CircularProgressIndicator(modifier = modifier.semantics {
                        this.contentDescription = "loading-$contentDescription"
                    })
                }

                is AsyncImagePainter.State.Error -> {
                    Log.v(LOG_TAG, "AlbumArtAsync() PainterState: ERROR")
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = "error-$contentDescription",
                        modifier = modifier
                    )
                }

                is AsyncImagePainter.State.Success -> {
                    Log.v(LOG_TAG, "AlbumArtAsync() PainterState: SUCCESS")
                    SubcomposeAsyncImageContent(modifier = modifier)
                }

                else -> {
                    Log.v(LOG_TAG, "AlbumArtAsync() PainterState: OTHER")
                    Icon(
                        Icons.Filled.Album,
                        contentDescription = contentDescription,
                        modifier = modifier
                    )
                }
            }
        }
    }
}
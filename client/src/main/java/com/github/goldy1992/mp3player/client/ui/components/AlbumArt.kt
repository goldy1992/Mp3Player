package com.github.goldy1992.mp3player.client.ui.components

import android.net.Uri
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

private const val logTag = "AlbumArtAsync"

@Composable
fun AlbumArtAsync(uri : Uri,
                  contentDescription: String,
                  modifier : Modifier = Modifier
) {
    Log.d(logTag, "Invoked with Uri: ${uri.path}")
    SubcomposeAsyncImage(
        model = uri,
        contentDescription = contentDescription
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                Log.d(logTag, "PainterState: LOADING")
                CircularProgressIndicator(modifier = modifier.semantics {
                    this.contentDescription = "loading-$contentDescription"
                })
            }
            is AsyncImagePainter.State.Error -> {
                Log.d(logTag, "PainterState: ERROR")
                Icon(Icons.Filled.Error, contentDescription = "error-$contentDescription", modifier = modifier)
            }
            is AsyncImagePainter.State.Success -> {
                Log.d(logTag, "PainterState: SUCCESS")
                SubcomposeAsyncImageContent()
            }
            else -> {
                Log.d(logTag, "PainterState: OTHER")
                Icon(Icons.Filled.Album, contentDescription=contentDescription, modifier=modifier)
            }
        }
    }
}
package com.github.goldy1992.mp3player.client.ui.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.goldy1992.mp3player.commons.MediaItemType

@Composable
fun NoResultsFound(mediaItemType: MediaItemType,
                    modifier: Modifier = Modifier) {
    val noResultsText = "No ${mediaItemType.title} found on device."
    Column(modifier.fillMaxSize()) {
        Text(text = noResultsText)
    }
}
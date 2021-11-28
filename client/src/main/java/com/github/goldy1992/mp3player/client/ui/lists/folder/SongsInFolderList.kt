package com.github.goldy1992.mp3player.client.ui.lists.folder

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.goldy1992.mp3player.client.ui.SongList
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@Composable
fun SongsInFolderList(
        folder : MediaBrowserCompat.MediaItem,
        songsInFolders : List<MediaBrowserCompat.MediaItem>,
        showHeader : Boolean = true,
        onFolderItemSelected: (folder : MediaBrowserCompat.MediaItem) -> Unit,
       ) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (showHeader) {
            Text(text = "${MediaItemUtils.getDirectoryName(folder)}")
        }
        SongList(songs = songsInFolders, onFolderItemSelected)
    }

}
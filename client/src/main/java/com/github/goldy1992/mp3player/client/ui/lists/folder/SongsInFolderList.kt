package com.github.goldy1992.mp3player.client.ui.lists.folder

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList

@Composable
fun SongsInFolderList(
        folder : MediaBrowserCompat.MediaItem,
        songsInFolders : List<MediaBrowserCompat.MediaItem>,
        showHeader : Boolean = true,
        mediaController : MediaControllerAdapter,
        onFolderItemSelected: (folder : MediaBrowserCompat.MediaItem) -> Unit,
       ) {
    Column(modifier = Modifier.fillMaxSize()) {
        SongList(songs = songsInFolders, mediaControllerAdapter = mediaController, onFolderItemSelected)
    }

}
package com.github.goldy1992.mp3player.client.ui.lists.folder

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList

@Composable
fun SongsInFolderList(
        folder : MediaBrowserCompat.MediaItem,
        songsInFolders : LiveData<List<MediaBrowserCompat.MediaItem>>,
        showHeader : Boolean = true,
        mediaController : MediaControllerAdapter,
        onFolderItemSelected: (folder : MediaBrowserCompat.MediaItem) -> Unit,
       ) {
    val songs by songsInFolders.observeAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        SongList(songs = songs!!, mediaControllerAdapter = mediaController, onFolderItemSelected)
    }

}
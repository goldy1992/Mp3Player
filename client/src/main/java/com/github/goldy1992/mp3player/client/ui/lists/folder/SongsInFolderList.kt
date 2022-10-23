package com.github.goldy1992.mp3player.client.ui.lists.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SongsInFolderList(
    folder : MediaItem,
   // songsInFolders : LiveData<List<MediaItem>>,
    showHeader : Boolean = true,
    mediaController : MediaControllerAdapter,
    metadataState: StateFlow<MediaMetadata>,
    isPlayingState: StateFlow<Boolean>,
    onFolderItemSelected: (folder : MediaItem) -> Unit,
       ) {
    val songs = emptyList<MediaItem>()
    Column(modifier = Modifier.fillMaxSize()) {
        SongList(songs = songs!!, mediaControllerAdapter = mediaController, onSongSelected = onFolderItemSelected, metadataState = metadataState, isPlayingState = isPlayingState)
    }

}
package com.github.goldy1992.mp3player.client.ui.lists.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList

@Composable
fun SongsInFolderList(
    folder : MediaItem,
   // songsInFolders : LiveData<List<MediaItem>>,
    showHeader : Boolean = true,
    mediaController : MediaControllerAdapter,
    metadataFlow: MetadataFlow,
    isPlayingFlow: IsPlayingFlow,
    onFolderItemSelected: (folder : MediaItem) -> Unit,
       ) {
    val songs = emptyList<MediaItem>()
    Column(modifier = Modifier.fillMaxSize()) {
        SongList(songs = songs!!, mediaControllerAdapter = mediaController, onSongSelected = onFolderItemSelected, metadataFlow = metadataFlow, isPlayingFlow = isPlayingFlow)
    }

}
package com.github.goldy1992.mp3player.client.ui.lists.folder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.viewmodels.states.CurrentMediaItemState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SongsInFolderList(
    currentMediaItemState : StateFlow<MediaItem>,
    isPlayingState: StateFlow<Boolean>,
    onFolderItemSelected: (folder : MediaItem) -> Unit,
       ) {
    val songs = emptyList<MediaItem>()
    Column(modifier = Modifier.fillMaxSize()) {
        SongList(
            songs = songs,
            onSongSelected = onFolderItemSelected,
            isPlayingState = isPlayingState,
            currentMediaItemState = currentMediaItemState)
    }

}
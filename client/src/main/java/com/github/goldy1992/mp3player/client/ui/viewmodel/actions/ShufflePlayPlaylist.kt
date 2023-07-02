package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.os.Bundle
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.launch

interface ShufflePlayPlaylist : MediaViewModelBase {

    fun shufflePlayPlaylist(playlist: Playlist, startIndex : Int) {
        val mediaItems = playlist.songs.map { MediaItemBuilder(it.id).build() }
        val extras = Bundle()
        extras.putString(Constants.PLAYLIST_ID, MediaItemType.SONGS.name)

        val mediaMetadata = MediaMetadata.Builder()
            .setAlbumTitle(MediaItemType.SONGS.name)
            .setExtras(extras)
            .build()
        scope.launch {
            mediaRepository.setShuffleMode(true)
            mediaRepository.playFromPlaylist(mediaItems, startIndex, mediaMetadata) }

    }
}
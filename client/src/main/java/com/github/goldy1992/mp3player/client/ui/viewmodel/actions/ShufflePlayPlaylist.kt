package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import android.os.Bundle
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.launch

interface ShufflePlayPlaylist : MediaViewModelBase {

    fun shufflePlayPlaylist(playlist: Playlist, startIndex : Int) {
        val mediaItems = playlist.songs.map { MediaItemBuilder(it.id).build() }
        val extras = Bundle()
        extras.putString(Constants.PLAYLIST_ID, MediaItemType.SONGS.name)


        scope.launch {
            mediaRepository.setShuffleMode(true)
            mediaRepository.playPlaylist(playlist, startIndex) }

    }
}
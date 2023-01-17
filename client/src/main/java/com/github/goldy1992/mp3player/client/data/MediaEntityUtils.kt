package com.github.goldy1992.mp3player.client.data

import android.net.Uri
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.MediaItemUtils

object MediaEntityUtils {

    fun createSong(mediaItem : MediaItem) : Song {
        return Song(
            id = mediaItem.mediaId,
            title = MediaItemUtils.getTitle(mediaItem),
            artist = MediaItemUtils.getArtist(mediaItem),
            duration = MediaItemUtils.getDuration(mediaItem)
        )
    }

    fun createSongs(
        state : State,
        mediaItems: List<MediaItem>
    ) : Songs {
        val songs = mediaItems.map { createSong(it) }
        return Songs(
            state = state,
            songs = songs
        )
    }


    fun createAlbum(mediaItem : MediaItem,
                    songs: Songs = Songs()
    ) : Album {
        return Album(
            albumTitle = MediaItemUtils.getAlbumTitle(mediaItem),
            albumArtist = MediaItemUtils.getAlbumArtist(mediaItem),
            recordingYear = MediaItemUtils.getAlbumRecordingYear(mediaItem),
            releaseYear = MediaItemUtils.getAlbumReleaseYear(mediaItem),
            albumArt = MediaItemUtils.getAlbumArtUri(mediaItem) ?: Uri.EMPTY,
            songs = songs
        )
    }

    fun createAlbums(
        state : State,
        mediaItems: List<MediaItem>
    ) : Albums {
        val albums = mediaItems.map { createAlbum(it) }
        return Albums(
            state = state,
            albums = albums
        )
    }

    fun createFolder(mediaItem : MediaItem,
                    songs: Songs = Songs()
    ) : Folder {
        return Folder(
            name = MediaItemUtils.getDirectoryName(mediaItem),
            path = MediaItemUtils.getDirectoryPath(mediaItem),
            uri = MediaItemUtils.getDirectoryUri(mediaItem) ?: Uri.EMPTY,
            songs = songs
        )
    }

    fun createFolders(
        state : State,
        mediaItems: List<MediaItem>
    ) : Folders {
        val folders = mediaItems.map { createFolder(it) }
        return Folders(
            state = state,
            folders = folders
        )
    }
}
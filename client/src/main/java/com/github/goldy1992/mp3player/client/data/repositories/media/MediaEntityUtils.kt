package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.Albums
import com.github.goldy1992.mp3player.client.data.Folder
import com.github.goldy1992.mp3player.client.data.Folders
import com.github.goldy1992.mp3player.client.data.MediaEntity
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.client.data.RootChild
import com.github.goldy1992.mp3player.client.data.RootChildren
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemUtils

object MediaEntityUtils {

    fun createSong(mediaItem : MediaItem) : Song {
        return Song(
            id = mediaItem.mediaId,
            title = MediaItemUtils.getTitle(mediaItem),
            artist = MediaItemUtils.getArtist(mediaItem),
            duration = MediaItemUtils.getDuration(mediaItem),
            albumArt = MediaItemUtils.getAlbumArtUri(mediaItem) ?: Uri.EMPTY
        )
    }

    fun createMediaItem(song: Song) : MediaItem {
        return MediaItem.Builder()
            .setMediaId(song.id)
            .build()
    }

    fun createPlaylist(
        state : State,
        mediaItems: List<MediaItem>,
        id : String = Constants.UNKNOWN
    ) : Playlist {
        val songs = mediaItems.map { createSong(it) }
        val duration = songs.sumOf { it.duration }
        return Playlist(
            state = state,
            songs = songs,
            totalDuration = duration,
            id = id
        )
    }


    fun createAlbum(mediaItem : MediaItem,
                    playlist: Playlist = Playlist()
    ) : Album {
        return Album(
            id = mediaItem.mediaId,
            albumTitle = MediaItemUtils.getAlbumTitle(mediaItem),
            albumArtist = MediaItemUtils.getAlbumArtist(mediaItem),
            recordingYear = MediaItemUtils.getAlbumRecordingYear(mediaItem),
            releaseYear = MediaItemUtils.getAlbumReleaseYear(mediaItem),
            albumArt = MediaItemUtils.getAlbumArtUri(mediaItem) ?: Uri.EMPTY,
          //  playlist = playlist
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
                     playlist: Playlist = Playlist()
    ) : Folder {
        return Folder(
            id = mediaItem.mediaId,
            name = MediaItemUtils.getDirectoryName(mediaItem),
            encodedLibraryId = Uri.encode(mediaItem.mediaId),
            path = MediaItemUtils.getDirectoryPath(mediaItem),
            encodedPath = Uri.encode(MediaItemUtils.getDirectoryPath(mediaItem)),
            uri = MediaItemUtils.getDirectoryUri(mediaItem) ?: Uri.EMPTY,
            playlist = playlist
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

    private fun createRootItem(mediaItem : MediaItem) : RootChild {
        return RootChild(
            id = mediaItem.mediaId,
            type = MediaItemUtils.getMediaItemType(mediaItem)
        )
    }

    fun createRootItems(
        state : State,
        mediaItems: List<MediaItem>
    ) : RootChildren {
        val rootItems = mediaItems.map { createRootItem(it) }
        return RootChildren(
            state = state,
            items = rootItems
        )
    }

    fun mapMediaItemsToMediaEntities(mediaItems : List<MediaItem>) : List<MediaEntity> {
        return emptyList()
    }
}
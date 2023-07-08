package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.State

object PlaylistCopier {

    fun populateAlbum(album: Album, playlist: Playlist) : Album {
        return Album(
            id = album.id,
            title = album.title,
            artist = album.artist,
            recordingYear = album.recordingYear,
            releaseYear = album.releaseYear,
            playlist = playlist,
            artworkUri = album.artworkUri
        )
    }

    fun populateFolder(folder: Folder, playlist: Playlist) : Folder {
        return Folder(
            id = folder.id,
            encodedLibraryId = folder.encodedLibraryId,
            name= folder.name,
            path = folder.path,
            encodedPath = folder.encodedPath,
            uri = folder.uri,
            playlist = playlist,
            state = State.LOADED,
        )
    }
}
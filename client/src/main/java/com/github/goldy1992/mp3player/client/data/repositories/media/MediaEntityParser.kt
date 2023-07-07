package com.github.goldy1992.mp3player.client.data.repositories.media

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.Album
import com.github.goldy1992.mp3player.client.models.Albums
import com.github.goldy1992.mp3player.client.models.Folder
import com.github.goldy1992.mp3player.client.models.Folders
import com.github.goldy1992.mp3player.client.models.MediaEntity
import com.github.goldy1992.mp3player.client.models.Playlist
import com.github.goldy1992.mp3player.client.models.Root
import com.github.goldy1992.mp3player.client.models.Song


object MediaEntityParser {

    @Suppress("UNCHECKED_CAST")
    fun <T : MediaEntity> parse(
    parent: T,
    mediaItems : List<MediaItem>) : T {
        if (parent is Root) {
            return MediaEntityUtils.createRootChildren(parent, mediaItems) as T
        }
        if (parent is Albums) {
            return MediaEntityUtils.createAlbums(parent, mediaItems) as T
        }

        if (parent is Album) {
            return MediaEntityUtils.createAlbum(parent, mediaItems) as T
        }

        if (parent is Folders) {
            return MediaEntityUtils.createFolders(parent, mediaItems) as T
        }

        if (parent is Folder) {
            return MediaEntityUtils.createFolder(parent, mediaItems) as T
        }

        if (parent is Playlist) {
            return MediaEntityUtils.createPlaylist(parent, mediaItems) as T
        }



        return Song() as T
    }
}
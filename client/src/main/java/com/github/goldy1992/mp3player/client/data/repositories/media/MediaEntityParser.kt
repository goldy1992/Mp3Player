package com.github.goldy1992.mp3player.client.data.repositories.media

import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.Album
import com.github.goldy1992.mp3player.client.models.Albums
import com.github.goldy1992.mp3player.client.models.Folder
import com.github.goldy1992.mp3player.client.models.Folders
import com.github.goldy1992.mp3player.client.models.MediaEntity
import com.github.goldy1992.mp3player.client.models.Playlist
import com.github.goldy1992.mp3player.client.models.Root
import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.commons.LogTagger


object MediaEntityParser : LogTagger {

    @Suppress("UNCHECKED_CAST")
    fun <T : MediaEntity> parse(
    parent: T,
    mediaItems : List<MediaItem>) : T {
        Log.v(logTag(), "parse() invoked with parent: ${parent.javaClass} and ${mediaItems.size} mediaItems")
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


        Log.w(logTag(), "parent of type: ${parent.javaClass} does not contain an implementation! Returning a Song()")
        return Song() as T
    }

    override fun logTag(): String {
        return "MediaEntityParser"
    }
}
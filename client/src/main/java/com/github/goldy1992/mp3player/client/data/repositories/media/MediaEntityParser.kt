package com.github.goldy1992.mp3player.client.data.repositories.media

import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.Song
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
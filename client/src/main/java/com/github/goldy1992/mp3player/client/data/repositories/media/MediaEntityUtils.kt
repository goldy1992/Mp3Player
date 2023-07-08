package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.PlaylistCopier.populateAlbum
import com.github.goldy1992.mp3player.client.models.PlaylistCopier.populateFolder
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import java.util.EnumMap

object MediaEntityUtils : LogTagger {

    fun createRootChildren(root: Root, mediaItems : List<MediaItem>) : Root {
        return if (mediaItems.isEmpty()) {
            Root(
                id=root.id,
                state = State.NO_RESULTS,
                childMap = EnumMap(MediaItemType::class.java)
            )
        }
        else {
            val rootChildMap : EnumMap<MediaItemType, MediaEntity> = EnumMap(MediaItemType::class.java)
            for (mediaItem: MediaItem in mediaItems) {
                val mediaItemId = mediaItem.mediaId
                when (val mediaItemType = MediaItemUtils.getMediaItemType(mediaItem)) {
                    MediaItemType.ALBUMS -> rootChildMap[MediaItemType.ALBUMS] =
                        Albums(id = mediaItemId, state = State.LOADING)

                    MediaItemType.SONGS -> rootChildMap[MediaItemType.SONGS] =
                        Playlist(id = mediaItemId, state = State.LOADING)

                    MediaItemType.FOLDERS -> rootChildMap[MediaItemType.FOLDERS] =
                        Folders(id = mediaItemId, state = State.LOADING)

                    else -> Log.w(
                        "MediaEntityUtils",
                        "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded."
                    )
                }
            }
             Root(
                id = root.id,
                 state= State.LOADED,
                childMap = EnumMap(rootChildMap)
            )
        }

    }

    fun createAlbums(currentAlbum: Albums, mediaItems : List<MediaItem>) : Albums {
        val albums : List<Album> = mediaItems.map { createAlbum(it) }.toList()
        val state = if (albums.isEmpty()) State.NO_RESULTS else State.LOADED
        return Albums(currentAlbum.id, albums, state)
    }

    fun createAlbum(currentAlbum: Album, albumChildren : List<MediaItem>) : Album {
        val albumPlaylist = createPlaylist(currentAlbum.playlist, albumChildren)
        return populateAlbum(currentAlbum, albumPlaylist)
    }




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
        playlist: Playlist,
        mediaItems: List<MediaItem>,
    ) : Playlist {
        val songs = mediaItems.map { createSong(it) }
        val duration = songs.sumOf { it.duration }
        val state = if (songs.isEmpty()) State.NO_RESULTS else State.LOADED

        return Playlist(
            id = playlist.id,
            state = state,
            songs = songs,
            duration = duration,
        )
    }
    
    fun createFolder(folder : Folder,
                     folderChildren : List<MediaItem>) : Folder {
        val folderPlaylist = createPlaylist(folder.playlist, folderChildren)
        return populateFolder(folder, folderPlaylist)
    }
    


    fun createAlbum(mediaItem : MediaItem,
                    playlist: Playlist = Playlist()
    ) : Album {
        return Album(
            id = mediaItem.mediaId,
            title = MediaItemUtils.getAlbumTitle(mediaItem),
            artist = MediaItemUtils.getAlbumArtist(mediaItem),
            recordingYear = MediaItemUtils.getAlbumRecordingYear(mediaItem),
            releaseYear = MediaItemUtils.getAlbumReleaseYear(mediaItem),
            artworkUri = MediaItemUtils.getAlbumArtUri(mediaItem) ?: Uri.EMPTY,
            playlist = playlist
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
        folders: Folders,
        mediaItems: List<MediaItem>
    ) : Folders {
        val foldersList = mediaItems.map { createFolder(it) }
        val state = if (foldersList.isEmpty()) State.NO_RESULTS else State.LOADED
        return Folders(folders.id, state, foldersList
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : MediaEntity> setStateNoPermissions(mediaEntity: T) : T  {
        if (mediaEntity is Root) {
            return Root(
                id = mediaEntity.id,
                childMap = mediaEntity.childMap,
                state = State.NO_PERMISSIONS
            ) as T
        }

        if (mediaEntity is Folders) {
            return Folders(
                id = mediaEntity.id,
                folders = mediaEntity.folders,
                state = State.NO_PERMISSIONS
            ) as T
        }

        if (mediaEntity is Albums) {
            return Albums(
                id = mediaEntity.id,
                albums = mediaEntity.albums,
                state = State.NO_PERMISSIONS
            ) as T
        }

        if (mediaEntity is Playlist) {
            return Playlist(
                id = mediaEntity.id,
                songs = mediaEntity.songs,
                duration = mediaEntity.duration,
                state = State.NO_PERMISSIONS
            ) as T
        }

        Log.w(logTag(), "setStateNoPermissions() invoked with unsupported MediaEntity: ${mediaEntity.javaClass}, returning the original object!")
        return mediaEntity
    }

    override fun logTag(): String {
        return "MediaEntityUtils"
    }
}
package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.util.Log
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.models.Album
import com.github.goldy1992.mp3player.client.models.Albums
import com.github.goldy1992.mp3player.client.models.Folder
import com.github.goldy1992.mp3player.client.models.Folders
import com.github.goldy1992.mp3player.client.models.MediaEntity
import com.github.goldy1992.mp3player.client.models.Playlist
import com.github.goldy1992.mp3player.client.models.PlaylistCopier.populateAlbum
import com.github.goldy1992.mp3player.client.models.PlaylistCopier.populateFolder
import com.github.goldy1992.mp3player.client.models.Root
import com.github.goldy1992.mp3player.client.models.RootChild
import com.github.goldy1992.mp3player.client.models.RootChildren
import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.client.models.State
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import java.util.EnumMap

object MediaEntityUtils {

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

                    MediaItemType.ROOT -> rootChildMap[MediaItemType.ROOT] = RootChildren.LOADING
                    else -> Log.w(
                        "MediaEntityUtils",
                        "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded."
                    )
                }
            }
             Root(
                id = root.id,
                 state=State.LOADED,
                childMap = EnumMap(rootChildMap)
            )
        }

    }

    fun createAlbums(currentAlbum: Albums, mediaItems : List<MediaItem>) : Albums {
        val albums : List<Album> = mediaItems.map { createAlbum(it) }.toList()
        val state = if (albums.isEmpty()) State.NO_RESULTS else State.LOADED
        return Albums(currentAlbum.id, state, albums)
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
        songs: List<MediaItem>,
    ) : Playlist {
        val songs = songs.map { createSong(it) }
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
          //  playlist = playlist
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

    private fun createRootItem(mediaItem : MediaItem) : RootChild {
        return RootChild(
            id = mediaItem.mediaId,
            type = MediaItemUtils.getMediaItemType(mediaItem)
        )
    }

    fun createRootItems(
        state : State,
        rootChildren: List<RootChild>
    ) : RootChildren {
        return RootChildren(
            state = state,
            items = rootChildren
        )
    }

    fun mapMediaItemsToMediaEntities(mediaItems : List<MediaItem>) : List<MediaEntity> {
        return emptyList()
    }
}
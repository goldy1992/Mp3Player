package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.data.CustomCommandEvent
import com.github.goldy1992.mp3player.client.data.MediaEntity
import com.github.goldy1992.mp3player.client.data.PlaybackParameters
import com.github.goldy1992.mp3player.client.data.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.client.data.QueueChangedEvent
import com.github.goldy1992.mp3player.client.data.RepeatMode
import com.github.goldy1992.mp3player.client.data.Root
import com.github.goldy1992.mp3player.client.data.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun audioData() : Flow<AudioSample>

    fun currentSong() : Flow<Song>

   // fun currentPlaylistId() : Flow<String>

    fun currentSearchQuery() : Flow<String>

    fun isPlaying() : Flow<Boolean>

    fun isShuffleModeEnabled() : Flow<Boolean>

    fun onChildrenChanged() : Flow<ChildrenChangedEvent>

    fun onCustomCommand() : Flow<CustomCommandEvent>

    fun onSearchResultsChanged() : Flow<SearchResultsChangedEvent>

    fun playbackParameters() : Flow<PlaybackParameters>

    fun playbackPosition() : Flow<PlaybackPositionEvent>

    fun playbackSpeed() : Flow<Float>

    fun queue() : Flow<QueueChangedEvent>

    fun repeatMode() : Flow<RepeatMode>

    suspend fun changePlaybackSpeed(speed : Float)

    suspend fun getChildren(parentId : String,
                            @androidx.annotation.IntRange(from = 0) page : Int = 0,
                            @androidx.annotation.IntRange(from = 1) pageSize : Int = 20,
                            params: Bundle = Bundle(),
    ) : List<MediaEntity>

    suspend fun getLibraryRoot() : Root

    suspend fun getCurrentPlaybackPosition(): Long

    suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : List<MediaEntity>

    suspend fun pause()

    suspend fun play()

    suspend fun play(song: Song)


    suspend fun playAlbum(album: Album, startIndex : Int)

    suspend fun playPlaylist(playlist: Playlist, startIndex: Int)

    suspend fun playFromUri(uri: Uri?, extras: Bundle?)

    suspend fun prepareFromId(mediaId : String)

    suspend fun search(query: String, extras: Bundle)

    suspend fun seekTo(position : Long)

    suspend fun setRepeatMode(repeatMode: RepeatMode)

    suspend fun setShuffleMode(shuffleModeEnabled : Boolean)

    suspend fun skipToNext()

    suspend fun skipToPrevious()

    suspend fun stop()

    suspend fun subscribe(id : String)
}
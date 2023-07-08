package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IntRange
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.CustomCommandEvent
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.PlaybackParametersEvent
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun audioData() : Flow<AudioSample>

    fun currentSong() : Flow<Song>

    fun currentPlaylistId() : Flow<String>

    fun currentSearchQuery() : Flow<String>

    fun isPlaying() : Flow<Boolean>

    fun isShuffleModeEnabled() : Flow<Boolean>

    fun onChildrenChanged() : Flow<ChildrenChangedEvent>

    fun onCustomCommand() : Flow<CustomCommandEvent>

    fun onSearchResultsChanged() : Flow<SearchResultsChangedEvent>

    fun playbackParameters() : Flow<PlaybackParametersEvent>

    fun playbackPosition() : Flow<PlaybackPositionEvent>

    fun playbackSpeed() : Flow<Float>

    fun queue() : Flow<Queue>

    fun repeatMode() : Flow<RepeatMode>

    suspend fun changePlaybackSpeed(speed : Float)

    suspend fun <T : MediaEntity> getChildren(
        parent: T,
        @IntRange(from = 0) page: Int = 0,
        @IntRange(from = 1) pageSize: Int = 20,
        params: Bundle = Bundle(),
    ) : T

    suspend fun getLibraryRoot() : Root

    suspend fun getPlaylist(playlistId : String,
                            @IntRange(from = 0) page : Int = 0,
                            @IntRange(from = 1) pageSize : Int = 20,
                            params: Bundle = Bundle()) : Playlist

    suspend fun getCurrentPlaybackPosition(): Long

    suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : SearchResults

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
package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow

interface MediaRepository {

    fun audioData() : Flow<AudioSample>

    fun currentMediaItem() : Flow<MediaItem>

    fun currentSearchQuery() : Flow<String>

    fun isPlaying() : Flow<Boolean>

    fun isShuffleModeEnabled() : Flow<Boolean>

    fun metadata() : Flow<MediaMetadata>

    fun onChildrenChanged() : Flow<OnChildrenChangedEventHolder>

    fun onCustomCommand() : Flow<SessionCommandEventHolder>

    fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder>

    fun playbackParameters() : Flow<PlaybackParameters>

    fun playbackPosition() : Flow<PlaybackPositionEvent>

    fun playbackSpeed() : Flow<Float>

    fun queue() : Flow<QueueState>

    fun repeatMode() : Flow<@Player.RepeatMode Int>

    suspend fun changePlaybackSpeed(speed : Float)

    suspend fun getChildren(parentId : String,
                            @androidx.annotation.IntRange(from = 0) page : Int = 0,
                            @androidx.annotation.IntRange(from = 1) pageSize : Int = 20,
                            params : MediaLibraryService.LibraryParams = MediaLibraryService.LibraryParams.Builder().build()
    ) : List<MediaItem>

    suspend fun getLibraryRoot() : MediaItem

    suspend fun getSearchResults(query: String, page : Int = 0, pageSize : Int = 20) : List<MediaItem>

    suspend fun pause()

    suspend fun play()

    suspend fun play(mediaItem : MediaItem)

    suspend fun playFromSongList(itemIndex : Int, items : List<MediaItem>)

    suspend fun playFromUri(uri: Uri?, extras: Bundle?)

    suspend fun prepareFromMediaId(mediaItem: MediaItem)

    suspend fun search(query: String, extras: Bundle)

    suspend fun seekTo(position : Long)

    suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int)

    suspend fun setShuffleMode(shuffleModeEnabled : Boolean)

    suspend fun skipToNext()

    suspend fun skipToPrevious()

    suspend fun stop()

    suspend fun subscribe(id : String)
}
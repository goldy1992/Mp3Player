package com.github.goldy1992.mp3player.client.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionToken
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface IMediaBrowser {

    fun init(sessionToken: SessionToken, scope : CoroutineScope)

    fun audioData() : Flow<AudioSample>

    fun currentMediaItem() : Flow<MediaItem>

    fun currentPlaylistMetadata() : Flow<MediaMetadata>

    fun isPlaying() : Flow<Boolean>

    fun isShuffleModeEnabled() : Flow<Boolean>

    fun metadata() : Flow<MediaMetadata>

    fun onChildrenChanged() : Flow<OnChildrenChangedEventHolder>

    fun onCustomCommand() : Flow<SessionCommandEventHolder>

    fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder>

    fun playbackParameters() : Flow<PlaybackParameters>

    fun playbackPosition() : Flow<OnPlaybackPositionChangedEvent>

    fun playbackSpeed() : Flow<Float>

    fun queue() : Flow<OnQueueChangedEventHolder>

    fun repeatMode() : Flow<@Player.RepeatMode Int>

    suspend fun changePlaybackSpeed(speed : Float)

    suspend fun getCurrentPlaybackPosition() : Long

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

    suspend fun playFromPlaylist(items: List<MediaItem>, itemIndex: Int, playlistId: String)

    suspend fun playFromPlaylist(playlistId: String, itemIndex: Int)

    suspend fun playFromUri(uri: Uri?, extras: Bundle?)

    suspend fun prepareFromMediaId(mediaId: String)

    suspend fun search(query: String, extras: Bundle)

    suspend fun seekTo(position : Long)

    suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int)

    suspend fun setShuffleMode(shuffleModeEnabled : Boolean)

    suspend fun skipToNext()

    suspend fun skipToPrevious()

    suspend fun stop()

    suspend fun subscribe(id : String)

    fun release()

}
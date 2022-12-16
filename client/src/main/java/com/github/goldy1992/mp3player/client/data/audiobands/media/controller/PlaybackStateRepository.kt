package com.github.goldy1992.mp3player.client.data.audiobands.media.controller

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow

interface PlaybackStateRepository {

    fun audioData() : Flow<AudioSample>

    fun isPlaying() : Flow<Boolean>

    fun isShuffleModeEnabled() : Flow<Boolean>

    fun metadata() : Flow<MediaMetadata>

    fun playbackParameters() : Flow<PlaybackParameters>

    fun playbackPosition() : Flow<PlaybackPositionEvent>

    fun playbackSpeed() : Flow<Float>

    fun queue() : Flow<QueueState>

    fun repeatMode() : Flow<@Player.RepeatMode Int>

    suspend fun changePlaybackSpeed(speed : Float)

    suspend fun pause()

    suspend fun play()

    suspend fun play(mediaItem : MediaItem)

    suspend fun playFromSongList(itemIndex : Int, items : List<MediaItem>)

    suspend fun playFromUri(uri: Uri?, extras: Bundle?)

    suspend fun prepareFromMediaId(mediaItem: MediaItem)

    suspend fun seekTo(position : Long)

    suspend fun setRepeatMode(@Player.RepeatMode repeatMode: Int)

    suspend fun setShuffleMode(shuffleModeEnabled : Boolean)

    suspend fun skipToNext()

    suspend fun skipToPrevious()

    suspend fun stop()




}
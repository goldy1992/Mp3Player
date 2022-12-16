package com.github.goldy1992.mp3player.client.ui

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.PlaybackStateRepository
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestPlaybackStateRepository() : PlaybackStateRepository {
    override fun audioData(): Flow<AudioSample> {
        TODO("Not yet implemented")
    }

    val isPlayingValue = MutableStateFlow(true)
    override fun isPlaying(): Flow<Boolean> {
        return isPlayingValue
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun metadata(): Flow<MediaMetadata> {
        TODO("Not yet implemented")
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        TODO("Not yet implemented")
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        TODO("Not yet implemented")
    }

    override fun playbackSpeed(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override fun queue(): Flow<QueueState> {
        TODO("Not yet implemented")
    }

    override fun repeatMode(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        TODO("Not yet implemented")
    }

    override suspend fun pause() {
        TODO("Not yet implemented")
    }

    override suspend fun play() {
        TODO("Not yet implemented")
    }

    override suspend fun play(mediaItem: MediaItem) {
    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override suspend fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun skipToNext() {
        TODO("Not yet implemented")
    }

    override suspend fun skipToPrevious() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }
}
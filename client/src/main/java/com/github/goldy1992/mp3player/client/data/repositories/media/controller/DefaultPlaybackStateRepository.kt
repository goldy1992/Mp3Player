package com.github.goldy1992.mp3player.client.data.repositories.media.controller

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 *
 */
@ActivityRetainedScoped
class DefaultPlaybackStateRepository

    @Inject
    constructor(
        private val mediaDataSource: MediaDataSource

    ) : PlaybackStateRepository, LogTagger {

    override fun audioData(): Flow<AudioSample> {
        return mediaDataSource.audioData()
    }

    override fun currentMediaItem() : Flow<MediaItem> {
        return mediaDataSource.currentMediaItem()
    }

    override fun isPlaying(): Flow<Boolean> {
        return mediaDataSource.isPlaying()
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return mediaDataSource.isShuffleModeEnabled()
    }

    override fun metadata(): Flow<MediaMetadata> {
        return mediaDataSource.metadata()
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
       return mediaDataSource.playbackParameters()
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return mediaDataSource.playbackPosition()
    }

    override fun playbackSpeed(): Flow<Float> {
        return mediaDataSource.playbackSpeed()
    }

    override fun queue(): Flow<QueueState> {
        return mediaDataSource.queue()
    }

    override fun repeatMode(): Flow<Int> {
        return mediaDataSource.repeatMode()
    }

    override suspend fun changePlaybackSpeed(speed: Float) {     val extras = Bundle()
        mediaDataSource.changePlaybackSpeed(speed)
    }

    override suspend fun pause() {
        mediaDataSource.pause()
    }

    override suspend fun play() {
        mediaDataSource.play()
    }

    override suspend fun play(mediaItem: MediaItem) {
        mediaDataSource.play(mediaItem)
    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        mediaDataSource.playFromSongList(itemIndex, items)
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        mediaDataSource.playFromUri(uri, extras)
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        mediaDataSource.prepareFromMediaId(mediaItem)
    }

    override suspend fun seekTo(position: Long) {
        mediaDataSource.seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        mediaDataSource.setRepeatMode(repeatMode)
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        mediaDataSource.setShuffleMode(shuffleModeEnabled)
    }

    override suspend fun skipToNext() {
        mediaDataSource.skipToNext()
    }

    override suspend fun skipToPrevious() {
        mediaDataSource.skipToPrevious()
    }

    override suspend fun stop() {
        mediaDataSource.stop()
    }

    override fun logTag(): String {
        return "DefaultPlaybackStateRepo"
    }
}
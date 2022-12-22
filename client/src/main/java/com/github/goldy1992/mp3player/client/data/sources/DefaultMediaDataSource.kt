package com.github.goldy1992.mp3player.client.data.sources

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.SessionCommand
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.ui.flows.player.*
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class DefaultMediaDataSource

    @Inject
    constructor(
        private val mediaBrowser : IMediaBrowser,
        private val mediaControllerFuture : ListenableFuture<MediaBrowser>,
        private val audioDataFlow: AudioDataFlow,
        private val currentMediaItemFlow: CurrentMediaItemFlow,
        private val isPlayingFlow: IsPlayingFlow,
        private val metadataFlow: MetadataFlow,
        private val playbackParametersFlow: PlaybackParametersFlow,
        private val playbackPositionFlow: PlaybackPositionFlow,
        private val playbackSpeedFlow: PlaybackSpeedFlow,
        private val queueFlow: QueueFlow,
        private val repeatModeFlow: RepeatModeFlow,
        private val shuffleModeFlow: ShuffleModeFlow
    )
    : MediaDataSource, LogTagger {

    override fun audioData(): Flow<AudioSample> {
        return audioDataFlow.flow()
    }

    override fun currentMediaItem() : Flow<MediaItem> {
        return currentMediaItemFlow.flow()
    }

    override fun currentSearchQuery(): Flow<String> {
        TODO("Not yet implemented")
    }

    override fun isPlaying(): Flow<Boolean> {
        return isPlayingFlow.flow()
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return shuffleModeFlow.flow()
    }

    override fun metadata(): Flow<MediaMetadata> {
        return metadataFlow.flow()
    }

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return mediaBrowser.onChildrenChanged()
    }

    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        TODO("Not yet implemented")
    }

    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        TODO("Not yet implemented")
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        return playbackParametersFlow.flow()
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return playbackPositionFlow.flow()
    }

    override fun playbackSpeed(): Flow<Float> {
        return playbackSpeedFlow.flow()
    }

    override fun queue(): Flow<QueueState> {
        return queueFlow.flow()
    }

    override fun repeatMode(): Flow<Int> {
        return repeatModeFlow.flow()
    }

    override suspend fun changePlaybackSpeed(speed: Float) {     val extras = Bundle()
        extras.putFloat(Constants.CHANGE_PLAYBACK_SPEED, speed)
        val changePlaybackSpeedCommand = SessionCommand(Constants.CHANGE_PLAYBACK_SPEED, extras)
        mediaControllerFuture.await().sendCustomCommand(changePlaybackSpeedCommand, extras).await()
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getLibraryRoot(): MediaItem {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        TODO("Not yet implemented")
    }

    override suspend fun pause() {
        mediaControllerFuture.await().pause()
    }

    override suspend fun play() {
        val future = mediaControllerFuture.await()
        Log.i(logTag(), "awaiting future for play")
        future.play()
        Log.i(logTag(), "calling play")
    }

    override suspend fun play(mediaItem: MediaItem) {
        val mediaController = mediaControllerFuture.await()
        mediaController.addMediaItem(mediaItem)
        mediaController.prepare()
        mediaController.play()
    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        val mediaController = mediaControllerFuture.await()
        mediaController.clearMediaItems()
        mediaController.addMediaItems(items)
        mediaController.seekTo(itemIndex, 0L)
        mediaController.prepare()
        mediaController.play()
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        val mediaItem = MediaItem.Builder().setUri(uri).build()
        mediaControllerFuture.await().addMediaItem(mediaItem)
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        val mediaController = mediaControllerFuture.await()
        mediaController.addMediaItem(mediaItem)
        mediaController.prepare()
    }

    override suspend fun search(query: String, extras: Bundle) {
        TODO("Not yet implemented")
    }

    override suspend fun seekTo(position: Long) {
        mediaControllerFuture.await().seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        mediaControllerFuture.await().repeatMode = repeatMode
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        mediaControllerFuture.await().shuffleModeEnabled = shuffleModeEnabled
    }

    override suspend fun skipToNext() {
        mediaControllerFuture.await().seekToNextMediaItem()
    }

    override suspend fun skipToPrevious() {
        mediaControllerFuture.await().seekToPreviousMediaItem()
    }

    override suspend fun stop() {
        mediaControllerFuture.await().stop()
    }

    override suspend fun subscribe(id: String) {
        mediaControllerFuture.await().subscribe(id, MediaLibraryService.LibraryParams
            .Builder()
            .build())
        TODO("Not yet implemented")
    }

    override fun logTag(): String {
        return "DefaultPlaybackDataStore"
    }
}
package com.github.goldy1992.mp3player.client.data.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class DefaultMediaRepository
    @Inject
    constructor(
        private val mediaDataSource : MediaDataSource
    ) : MediaRepository, LogTagger {

    override fun audioData(): Flow<AudioSample> {
        return mediaDataSource.audioData()
    }

    override fun currentMediaItem(): Flow<MediaItem> {
        return mediaDataSource.currentMediaItem()
    }

    override fun currentSearchQuery(): Flow<String> {
        return mediaDataSource.currentSearchQuery()
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

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return mediaDataSource.onChildrenChanged()
    }

    override fun onCustomCommand() : Flow<SessionCommandEventHolder> {
        return mediaDataSource.onCustomCommand()
    }

    override fun onSearchResultsChanged() : Flow<OnSearchResultsChangedEventHolder> {
        return mediaDataSource.onSearchResultsChanged()
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

    override suspend fun changePlaybackSpeed(speed: Float) {
        mediaDataSource.changePlaybackSpeed(speed)
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return mediaDataSource.getChildren(parentId, page, pageSize, params)
    }

    override suspend fun getLibraryRoot(): MediaItem {
        return mediaDataSource.getLibraryRoot()
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int) : List<MediaItem> {
        return mediaDataSource.getSearchResults(query, page, pageSize)
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

    override suspend fun search(query: String, extras: Bundle) {
        mediaDataSource.search(query, extras)
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

    override suspend fun subscribe(id : String) {
        mediaDataSource.subscribe(id)
    }

    override fun logTag(): String {
        return "DefaultMediaBrowserRepo"
    }
}
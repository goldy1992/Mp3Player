package com.github.goldy1992.mp3player.client.data.sources

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.apache.commons.lang3.StringUtils.isEmpty
import javax.inject.Inject

@ActivityRetainedScoped
class DefaultMediaDataSource

    @Inject
    constructor(
        private val mediaBrowser : IMediaBrowser,

    )
    : MediaDataSource, LogTagger {

    override fun audioData(): Flow<AudioSample> {
        return mediaBrowser.audioData()
    }

    override fun currentMediaItem() : Flow<MediaItem> {
        return mediaBrowser.currentMediaItem()
    }

    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        return mediaBrowser.currentPlaylistMetadata()
    }

    private val _currentSearchQuery = MutableStateFlow("")
    private val currentSearchQuery : StateFlow<String> = _currentSearchQuery
    override fun currentSearchQuery(): Flow<String> {
        return currentSearchQuery
    }

    override fun isPlaying(): Flow<Boolean> {
        return mediaBrowser.isPlaying()
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return mediaBrowser.isShuffleModeEnabled()
    }


    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return mediaBrowser.onChildrenChanged()
    }

    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return mediaBrowser.onCustomCommand()
    }

    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return mediaBrowser.onSearchResultsChanged()
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        return mediaBrowser.playbackParameters()
    }

    override fun playbackPosition(): Flow<OnPlaybackPositionChangedEvent> {
        return mediaBrowser.playbackPosition()
    }

    override fun playbackSpeed(): Flow<Float> {
        return mediaBrowser.playbackSpeed()
    }

    override fun queue(): Flow<OnQueueChangedEventHolder> {
        return mediaBrowser.queue()
    }

    override fun repeatMode(): Flow<Int> {
        return mediaBrowser.repeatMode()
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        mediaBrowser.changePlaybackSpeed(speed)
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        return mediaBrowser.getCurrentPlaybackPosition()
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return mediaBrowser.getChildren(parentId, page, pageSize, params)
    }

    override suspend fun getLibraryRoot(): MediaItem {
        return mediaBrowser.getLibraryRoot()
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        return mediaBrowser.getSearchResults(query, page, pageSize)
    }

    override suspend fun pause() {
        mediaBrowser.pause()
    }

    override suspend fun play() {
        mediaBrowser.play()
    }

    override suspend fun play(mediaItem: MediaItem) {
        mediaBrowser.play(mediaItem)
    }

    override suspend fun playFromPlaylist(items: List<MediaItem>, itemIndex: Int, playlistId : String) {
        mediaBrowser.playFromPlaylist(items, itemIndex, playlistId)
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        mediaBrowser.playFromUri(uri, extras)
    }

    override suspend fun prepareFromMediaId(mediaId: String) {
        mediaBrowser.prepareFromMediaId(mediaId)
    }

    override suspend fun search(query: String, extras: Bundle) {
        this._currentSearchQuery.value = query

        if (isEmpty(query)) {
            Log.w(logTag(), "search() called with NULL or empty query")
        }
        else {
            mediaBrowser.search(query, extras)
        }

    }

    override suspend fun seekTo(position: Long) {
        mediaBrowser.seekTo(position)
    }

    override suspend fun setRepeatMode(repeatMode: Int) {
        mediaBrowser.setRepeatMode(repeatMode)
    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        mediaBrowser.setShuffleMode(shuffleModeEnabled)
    }

    override suspend fun skipToNext() {
        mediaBrowser.skipToNext()
    }

    override suspend fun skipToPrevious() {
        mediaBrowser.skipToPrevious()
    }

    override suspend fun stop() {
        mediaBrowser.stop()
    }

    override suspend fun subscribe(id: String) {
        mediaBrowser.subscribe(id)
    }

    override fun logTag(): String {
        return "DefaultMediaDataStore"
    }
}
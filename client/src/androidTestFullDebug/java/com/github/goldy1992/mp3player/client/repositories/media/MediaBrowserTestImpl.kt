package com.github.goldy1992.mp3player.client.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaBrowserTestImpl() : IMediaBrowser {
    override fun audioData(): Flow<AudioSample> {
        return flow {}
    }

    override fun currentMediaItem(): Flow<MediaItem> {
        return flow {}
    }

    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        return flow {}
    }

    override fun isPlaying(): Flow<Boolean> {
        return flow {}
    }

    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return flow {}
    }

    override fun metadata(): Flow<MediaMetadata> {
        return flow {}
    }

    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return flow {}
    }

    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return flow {}
    }

    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return flow {}
    }

    override fun playbackParameters(): Flow<PlaybackParameters> {
        return flow {}
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return flow {}
    }

    override fun playbackSpeed(): Flow<Float> {
        return flow {}
    }

    override fun queue(): Flow<QueueState> {
        return flow {}
    }

    override fun repeatMode(): Flow<Int> {
        return flow {}
    }

    override suspend fun changePlaybackSpeed(speed: Float) {
        
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        return 0L
    }

    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return emptyList()
    }

    override suspend fun getLibraryRoot(): MediaItem {
        return MediaItem.EMPTY
    }

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        return emptyList()
    }

    override suspend fun pause() {
        
    }

    override suspend fun play() {
        
    }

    override suspend fun play(mediaItem: MediaItem) {
        
    }

    override suspend fun playFromPlaylist(
        items: List<MediaItem>,
        itemIndex: Int,
        playlistMetadata: MediaMetadata
    ) {
        
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        
    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        
    }

    override suspend fun search(query: String, extras: Bundle) {
        
    }

    override suspend fun seekTo(position: Long) {
        
    }

    override suspend fun setRepeatMode(repeatMode: Int) {

    }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {

    }

    override suspend fun skipToNext() {

    }

    override suspend fun skipToPrevious() {

    }

    override suspend fun stop() {

    }

    override suspend fun subscribe(id: String) {

    }

    override fun release() {
    }
}
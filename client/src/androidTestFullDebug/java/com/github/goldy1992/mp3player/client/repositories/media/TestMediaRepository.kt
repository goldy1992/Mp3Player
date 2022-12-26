package com.github.goldy1992.mp3player.client.repositories.media

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * AndroidTest implementation of [MediaRepository]
 */
class TestMediaRepository
    @Inject
    constructor() : MediaRepository {

    override fun audioData(): Flow<AudioSample> {
        TODO("Not yet implemented")
    }

    val currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    override fun currentMediaItem(): Flow<MediaItem> {
        return currentMediaItemState
    }

    val currentSearchQuery = MutableStateFlow("")
    override fun currentSearchQuery(): Flow<String> {
        return currentSearchQuery
    }

    val isPlayingState = MutableStateFlow(false)
    override fun isPlaying(): Flow<Boolean> {
        return isPlayingState
    }

    val isShuffleModeEnabledState = MutableStateFlow(false)
    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return isShuffleModeEnabledState
    }

    val metadataState : MutableStateFlow<MediaMetadata> = MutableStateFlow(MediaMetadata.EMPTY)
    override fun metadata(): Flow<MediaMetadata> {
        return metadataState
    }

    val onChildrenChangedState = MutableStateFlow<OnChildrenChangedEventHolder>(
        OnChildrenChangedEventHolder.DEFAULT)
    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return onChildrenChangedState
    }


    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        TODO("Not yet implemented")
    }

    val searchResultsChangedState = MutableStateFlow(OnSearchResultsChangedEventHolder.DEFAULT)
    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return searchResultsChangedState
    }

    val playbackParametersState = MutableStateFlow(PlaybackParameters.DEFAULT)
    override fun playbackParameters(): Flow<PlaybackParameters> {
        return playbackParametersState
    }

    val playbackPositionEventState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)
    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return playbackPositionEventState
    }

    val playbackSpeedState = MutableStateFlow(1.0f)
    override fun playbackSpeed(): Flow<Float> {
        return playbackSpeedState
    }

    val queueState = MutableStateFlow(QueueState.EMPTY)
    override fun queue(): Flow<QueueState> {
        return queueState
    }

    val repeatModeState = MutableStateFlow<@RepeatMode Int>(Player.REPEAT_MODE_OFF)
    override fun repeatMode(): Flow<Int> {
        return repeatModeState
    }

    override suspend fun changePlaybackSpeed(speed: Float) {

    }

    val getChildrenState = MutableStateFlow<List<MediaItem>>(emptyList())
    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return getChildrenState.value
    }

    val libraryRootState = MutableStateFlow(MediaItem.EMPTY)
    override suspend fun getLibraryRoot(): MediaItem {
        return libraryRootState.value
    }

    val searchResultsState = MutableStateFlow<List<MediaItem>>(emptyList())
    var searchResults : List<MediaItem> = emptyList()
    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {
        return searchResults
    }

    override suspend fun pause() {
        TODO("Not yet implemented")
    }

    override suspend fun play() {
        TODO("Not yet implemented")
    }

    override suspend fun play(mediaItem: MediaItem) {    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {
        TODO("Not yet implemented")
    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {

    }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: String, extras: Bundle) {
        this.currentSearchQuery.value = query
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

    override suspend fun subscribe(id: String) {

    }
}
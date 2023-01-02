package com.github.goldy1992.mp3player.client.data.sources

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMediaDataSource : MediaDataSource {

    val audioDataState = MutableStateFlow(AudioSample.NONE)
    override fun audioData(): Flow<AudioSample> {
        return audioDataState
    }

    val currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    override fun currentMediaItem(): Flow<MediaItem> {
        return currentMediaItemState
    }

    val currentSearchQueryState = MutableStateFlow("")
    override fun currentSearchQuery(): Flow<String> {
        return currentSearchQueryState
    }

    val isPlayingState = MutableStateFlow(false)
    override fun isPlaying(): Flow<Boolean> {
        return isPlayingState
    }

    val isShuffleEnabledState = MutableStateFlow(false)
    override fun isShuffleModeEnabled(): Flow<Boolean> {
        return isShuffleEnabledState
    }

    val metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    override fun metadata(): Flow<MediaMetadata> {
        return metadataState
    }

    val onChildrenChangedState = MutableStateFlow(OnChildrenChangedEventHolder.DEFAULT)
    override fun onChildrenChanged(): Flow<OnChildrenChangedEventHolder> {
        return onChildrenChangedState
    }

    val onCustomCommandState = MutableStateFlow(SessionCommandEventHolder.DEFAULT)
    override fun onCustomCommand(): Flow<SessionCommandEventHolder> {
        return onCustomCommandState
    }

    val onSearchResultsChangedState = MutableStateFlow(OnSearchResultsChangedEventHolder.DEFAULT)
    override fun onSearchResultsChanged(): Flow<OnSearchResultsChangedEventHolder> {
        return onSearchResultsChangedState
    }

    val playbackParametersState = MutableStateFlow(PlaybackParameters.DEFAULT)
    override fun playbackParameters(): Flow<PlaybackParameters> {
        return playbackParametersState
    }

    var playbackPositionState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)
    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return playbackPositionState
    }

    val playbackSpeedState = MutableStateFlow(0f)
    override fun playbackSpeed(): Flow<Float> {
        return playbackSpeedState
    }

    val queueState = MutableStateFlow(QueueState.EMPTY)
    override fun queue(): Flow<QueueState> {
        return queueState
    }

    val repeatModeState = MutableStateFlow(0)
    override fun repeatMode(): Flow<Int> {
        return repeatModeState
    }

    override suspend fun changePlaybackSpeed(speed: Float) { }

    var getChildrenValue = emptyList<MediaItem>()
    override suspend fun getChildren(
        parentId: String,
        page: Int,
        pageSize: Int,
        params: MediaLibraryService.LibraryParams
    ): List<MediaItem> {
        return getChildrenValue
    }

    var libraryRootState = MediaItem.EMPTY
    override suspend fun getLibraryRoot(): MediaItem {
        return libraryRootState
    }

    var searchResultsState : List<MediaItem> = emptyList()
    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): List<MediaItem> {  return searchResultsState  }

    override suspend fun pause() {    }

    override suspend fun play() {    }

    override suspend fun play(mediaItem: MediaItem) {    }

    override suspend fun playFromSongList(itemIndex: Int, items: List<MediaItem>) {    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) { }

    override suspend fun prepareFromMediaId(mediaItem: MediaItem) {   }

    override suspend fun search(query: String, extras: Bundle) {  }

    override suspend fun seekTo(position: Long) {    }

    override suspend fun setRepeatMode(repeatMode: Int) {   }

    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {  }

    override suspend fun skipToNext() {  }

    override suspend fun skipToPrevious() { }

    override suspend fun stop() { }

    override suspend fun subscribe(id: String) {}
}
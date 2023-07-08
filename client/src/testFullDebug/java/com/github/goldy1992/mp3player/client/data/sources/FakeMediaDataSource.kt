package com.github.goldy1992.mp3player.client.data.sources

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnSearchResultsChangedEventHolder
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnPlaybackPositionChangedEvent
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicInteger

class FakeMediaDataSource : MediaDataSource {

    val audioDataState = MutableStateFlow(AudioSample.NONE)
    override fun audioData(): Flow<AudioSample> {
        return audioDataState
    }

    val currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    override fun currentMediaItem(): Flow<MediaItem> {
        return currentMediaItemState
    }

    override fun currentPlaylistMetadata(): Flow<MediaMetadata> {
        TODO("Not yet implemented")
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

    var playbackPositionState = MutableStateFlow(OnPlaybackPositionChangedEvent.DEFAULT)
    override fun playbackPosition(): Flow<OnPlaybackPositionChangedEvent> {
        return playbackPositionState
    }

    val playbackSpeedState = MutableStateFlow(0f)
    override fun playbackSpeed(): Flow<Float> {
        return playbackSpeedState
    }

    val onQueueChangedEventHolder = MutableStateFlow(OnQueueChangedEventHolder.EMPTY)
    override fun queue(): Flow<OnQueueChangedEventHolder> {
        return onQueueChangedEventHolder
    }

    val repeatModeState = MutableStateFlow(0)
    override fun repeatMode(): Flow<Int> {
        return repeatModeState
    }

    var playbackSpeed: Float? = null
    override suspend fun changePlaybackSpeed(speed: Float) {
        this.playbackSpeed = speed
    }
    override suspend fun getCurrentPlaybackPosition(): Long {
        TODO("Not yet implemented")
    }

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

    val pauseInvocations = AtomicInteger(0)
    override suspend fun pause() {
        pauseInvocations.incrementAndGet()
    }

    val playInvocations = AtomicInteger(0)
    override suspend fun play() {
        playInvocations.incrementAndGet()
    }

    override suspend fun play(mediaItem: MediaItem) {    }
    var playlistItems : List<MediaItem>? = null
    var playlistItemIndex : Int? = null
    var playlistId : String? = null
    override suspend fun playFromPlaylist(
        items: List<MediaItem>,
        itemIndex: Int,
        playlistId: String
    ) {
        this.playlistItems = items
        this.playlistItemIndex = itemIndex
        this.playlistId = playlistId
    }
    var uriToPlayFrom : Uri? = null
    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {
        this.uriToPlayFrom = uri
    }

    var idToPrepare : String? = null
    override suspend fun prepareFromMediaId(mediaId: String) {
        this.idToPrepare = mediaId
    }

    var searchQuery : String? = null
    override suspend fun search(query: String, extras: Bundle) {
        this.searchQuery = query
    }

    var seekToValue : Long? = null
    override suspend fun seekTo(position: Long) {
        this.seekToValue = position
    }

    @Player.RepeatMode
    var repeatMode : Int? = null
    override suspend fun setRepeatMode(repeatMode: Int) {
        this.repeatMode = repeatMode
    }

    var shuffleEnabled : Boolean = false
    override suspend fun setShuffleMode(shuffleModeEnabled: Boolean) {
        this.shuffleEnabled = shuffleModeEnabled
    }

    val skipToNextInvocations = AtomicInteger(0)

    override suspend fun skipToNext() {
        skipToNextInvocations.incrementAndGet()
    }
    val skipToPreviousInvocations = AtomicInteger(0)
    override suspend fun skipToPrevious() {
        skipToPreviousInvocations.incrementAndGet()
    }

    val stopInvocations = AtomicInteger(0)
    override suspend fun stop() {
        stopInvocations.incrementAndGet()
    }

    var subscribeId : String? = null
    override suspend fun subscribe(id: String) {
        this.subscribeId = id
    }
}
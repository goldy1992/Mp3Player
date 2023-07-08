package com.github.goldy1992.mp3player.client.repositories.media

import android.net.Uri
import android.os.Bundle
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.CustomCommandEvent
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.PlaybackParametersEvent
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.commons.AudioSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * AndroidTest implementation of [MediaRepository]
 */
class TestMediaRepository

    constructor() : MediaRepository {

    override fun audioData(): Flow<AudioSample> {
        TODO("Not yet implemented")
    }

    val currentMediaItemState = MutableStateFlow(Song.DEFAULT)
    override fun currentSong(): Flow<Song> {
        return currentMediaItemState
    }

    var currentPlaylistId = MutableStateFlow("")
    override fun currentPlaylistId(): Flow<String> {
        return currentPlaylistId
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


    var onChildrenChangedState = MutableStateFlow(ChildrenChangedEvent.DEFAULT)
    override fun onChildrenChanged(): Flow<ChildrenChangedEvent> {
        return onChildrenChangedState
    }


    val customCommandState = MutableStateFlow(CustomCommandEvent.DEFAULT)
    override fun onCustomCommand(): Flow<CustomCommandEvent> {
        return customCommandState
    }

    val searchResultsChangedState = MutableStateFlow(SearchResultsChangedEvent.DEFAULT)
    override fun onSearchResultsChanged(): Flow<SearchResultsChangedEvent> {
        return searchResultsChangedState
    }

    val playbackParametersState = MutableStateFlow(PlaybackParametersEvent.DEFAULT)
    override fun playbackParameters(): Flow<PlaybackParametersEvent> {
        return playbackParametersState
    }

    val onPlaybackPositionChangedEventState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)
    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        return onPlaybackPositionChangedEventState
    }

    val playbackSpeedState = MutableStateFlow(1.0f)
    override fun playbackSpeed(): Flow<Float> {
        return playbackSpeedState
    }

    val onQueueChangedEventHolder = MutableStateFlow(Queue.EMPTY)
    override fun queue(): Flow<Queue> {
        return onQueueChangedEventHolder
    }

    val repeatModeState = MutableStateFlow(RepeatMode.OFF)
    override fun repeatMode(): Flow<RepeatMode> {
        return repeatModeState
    }

    override suspend fun changePlaybackSpeed(speed: Float) {

    }
    var getChildrenState : MediaEntity? = null


    override suspend fun <T : MediaEntity> getChildren(
        parent: T,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): T {
        return getChildrenState!! as T
    }

    var libraryRootState = Root.NOT_LOADED
    override suspend fun getLibraryRoot(): Root {
        return libraryRootState
    }

    var playlist = Playlist.NOT_LOADED
    override suspend fun getPlaylist(
        playlistId: String,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): Playlist {
        return playlist
    }

    var currentPlaybackPosition = 0L
    override suspend fun getCurrentPlaybackPosition(): Long {
        return currentPlaybackPosition
    }

    var searchResults = SearchResults.NO_RESULTS

    override suspend fun getSearchResults(
        query: String,
        page: Int,
        pageSize: Int
    ): SearchResults {
        return searchResults
    }

    override suspend fun pause() {

    }

    override suspend fun play() {

    }

    override suspend fun play(song: Song) {

    }

    override suspend fun playAlbum(album: Album, startIndex: Int) {

    }

    override suspend fun playPlaylist(playlist: Playlist, startIndex: Int) {

    }

    override suspend fun playFromUri(uri: Uri?, extras: Bundle?) {

    }

    override suspend fun prepareFromId(mediaId: String) {
        TODO("Not yet implemented")
    }



    override suspend fun search(query: String, extras: Bundle) {
        this.currentSearchQuery.value = query
    }

    override suspend fun seekTo(position: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun setRepeatMode(repeatMode: RepeatMode) {
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
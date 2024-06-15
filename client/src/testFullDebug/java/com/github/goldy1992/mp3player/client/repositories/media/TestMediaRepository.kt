package com.github.goldy1992.mp3player.client.repositories.media

import android.net.Uri
import android.os.Bundle
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.ChildrenChangedEvent
import com.github.goldy1992.mp3player.client.models.CustomCommandEvent
import com.github.goldy1992.mp3player.client.models.PlaybackParametersEvent
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.Queue
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.SearchResultsChangedEvent
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.SearchResults
import com.github.goldy1992.mp3player.client.models.media.Song
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

    val currentSongState = MutableStateFlow(Song.DEFAULT)
    override fun currentSong(): Flow<Song> {
        return currentSongState
    }

    override fun currentPlaylistId(): Flow<String> {
        TODO("Not yet implemented")
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

    override fun onChildrenChanged(): Flow<ChildrenChangedEvent> {
        TODO("Not yet implemented")
    }

    override fun onCustomCommand(): Flow<CustomCommandEvent> {
        TODO("Not yet implemented")
    }

    override fun onSearchResultsChanged(): Flow<SearchResultsChangedEvent> {
        TODO("Not yet implemented")
    }

    override fun playbackParameters(): Flow<PlaybackParametersEvent> {
        TODO("Not yet implemented")
    }

    override fun playbackPosition(): Flow<PlaybackPositionEvent> {
        TODO("Not yet implemented")
    }


    override fun playbackSpeed(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override fun queue(): Flow<Queue> {
        TODO("Not yet implemented")
    }

    override fun repeatMode(): Flow<com.github.goldy1992.mp3player.client.models.RepeatMode> {
        TODO("Not yet implemented")
    }


    override suspend fun changePlaybackSpeed(speed: Float) {

    }

    override suspend fun <T : MediaEntity> getChildren(
        parent: T,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): T {
        TODO("Not yet implemented")
    }



    val libraryRootState = MutableStateFlow(Root.NOT_LOADED)
    override suspend fun getLibraryRoot(): Root {
        return libraryRootState.value
    }

    override suspend fun getPlaylist(
        playlistId: String,
        page: Int,
        pageSize: Int,
        params: Bundle
    ): Playlist {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentPlaybackPosition(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchResults(query: String, page: Int, pageSize: Int): SearchResults {
        TODO("Not yet implemented")
    }


    override suspend fun pause() {
        TODO("Not yet implemented")
    }

    override suspend fun play() {
        TODO("Not yet implemented")
    }

    override suspend fun play(song: Song) {
        TODO("Not yet implemented")
    }

    override suspend fun playAlbum(album: Album, startIndex: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun playPlaylist(playlist: Playlist, startIndex: Int) {
        TODO("Not yet implemented")
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